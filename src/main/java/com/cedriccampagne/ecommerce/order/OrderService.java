package com.cedriccampagne.ecommerce.order;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cedriccampagne.ecommerce.cartItem.CartItem;
import com.cedriccampagne.ecommerce.cartItem.CartItemRepository;
import com.cedriccampagne.ecommerce.order.dto.OrderDto;
import com.cedriccampagne.ecommerce.orderItem.OrderItem;
import com.cedriccampagne.ecommerce.user.User;
import com.cedriccampagne.ecommerce.user.UserRepository;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(
        OrderRepository orderRepository,
        UserRepository userRepository,
        CartItemRepository cartItemRepository
    ){
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(()-> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Commande introuvable"
            ));

        return OrderMapper.toOrderDto(order);
    }

    public List<OrderDto> getOrdersByUserId(Long userId) {
        userRepository.findById(userId)
            .orElseThrow(()-> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Utilisateur introuvable"
            ));

        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream()
                .map(OrderMapper::toOrderDto)
                .toList();
    }

    // Créer une commande a partir d'un panier par utilisateur
    @Transactional
    public OrderDto createOrderFromCart(Long userId) {

        //1 Vérifier que l’utilisateur existe
        User user = userRepository.findById(userId)
            .orElseThrow(()-> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Utilisateur introuvable"
            ));

        //2 Récupérer tous les CartItems de l’utilisateur
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        
        if (cartItems.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Le panier est vide");
        }

        //3 Calculer le total de la commande
        BigDecimal orderTotal = BigDecimal.ZERO;
        for (CartItem item  : cartItems) {
            BigDecimal price = item.getProduct().getPrice();
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());

            BigDecimal lineTotal = price.multiply(quantity);

            orderTotal = orderTotal.add(lineTotal);
        }

        //4 Créer l’entité Order (sans items pour l’instant), il faut créer pour avoir id et add les orderItems
        Order order = Order.builder()
            .user(user)
            .totalPrice(orderTotal)
            .items(new ArrayList<>())
            .build();

        Order savedOrder = orderRepository.save(order);

        //5 Créer les OrderItem (une ligne par CartItem)
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem item : cartItems) {
            OrderItem orderItem = OrderItem.builder()
                .order(savedOrder)
                .product(item.getProduct())
                .quantity(item.getQuantity())
                .priceAtPurchase(item.getProduct().getPrice())
                .build();

            orderItems.add(orderItem);
        }

        //6 Associer les OrderItem à l’Order
        savedOrder.setItems(orderItems);
        Order finalOrder = orderRepository.save(savedOrder);

        //7 Vider le panier
        cartItemRepository.deleteByUserId(userId);

        //8 Retourner un OrderDto complet
        return OrderMapper.toOrderDto(finalOrder);
    }
}

