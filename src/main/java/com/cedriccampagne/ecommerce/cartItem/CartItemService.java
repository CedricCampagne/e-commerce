package com.cedriccampagne.ecommerce.cartItem;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.cedriccampagne.ecommerce.cartItem.dto.CartItemCreateDto;
import com.cedriccampagne.ecommerce.cartItem.dto.CartItemDto;
import com.cedriccampagne.ecommerce.cartItem.dto.CartItemUpdateDto;
import com.cedriccampagne.ecommerce.user.User;
import com.cedriccampagne.ecommerce.user.UserRepository;

import com.cedriccampagne.ecommerce.product.Product;
import com.cedriccampagne.ecommerce.product.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {
    
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartItemService(
        CartItemRepository cartItemRepository,
        UserRepository userRepository,
        ProductRepository productRepository
    ) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<CartItemDto> getAllCartItems(){
        return cartItemRepository.findAll()
            .stream()
            .map(CartItemMapper::toCartItemDto)
            .toList();
    }

    public CartItemDto getCartItemById(Long id){
        CartItem cartItem = cartItemRepository.findById(id)
            .orElseThrow(()-> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "CartItem introuvable"
            ));
        
        return CartItemMapper.toCartItemDto(cartItem);
    }

    public CartItemDto createCartItem(CartItemCreateDto dto){
        User user = userRepository.findById(dto.userId())
            .orElseThrow(()-> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Utilisateur introuvable"
            ));

        Product product = productRepository.findById(dto.productId())
            .orElseThrow(()-> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Produit introuvable"
            ));

        Optional<CartItem> existing = cartItemRepository.findByUserIdAndProductId(dto.userId(), dto.productId());

        if(existing.isPresent()){
            CartItem existingItem = existing.get();

            existingItem.setQuantity(existingItem.getQuantity() + dto.quantity());

            return CartItemMapper.toCartItemDto(cartItemRepository.save(existingItem));
        } else {
            CartItem newItem = CartItemMapper.toEntityCartItem(dto);

            //!! Penser a associer le user et le product
            newItem.setUser(user);
            newItem.setProduct(product);

            CartItem saved = cartItemRepository.save(newItem);

            return CartItemMapper.toCartItemDto(saved);

        }
    }

    public CartItemDto updateCartItem(Long id, CartItemUpdateDto dto){

        CartItem cartItem = cartItemRepository.findById(id)
            .orElseThrow(
                ()-> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Utilisateur introuvable"
            ));

        cartItem.setQuantity(dto.quantity());

        CartItem updated = cartItemRepository.save(cartItem);

        return CartItemMapper.toCartItemDto(updated);
    }

    public void deleteCartItem(Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
            .orElseThrow(()-> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "CartItem introuvable"
            ));

        cartItemRepository.delete(cartItem);
    }

    public List<CartItemDto> getCartItemsByUserId(Long id){
        User user = userRepository.findById(id)
            .orElseThrow(()-> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Utilisateur introuvable"
            ));
        
        return cartItemRepository.findByUserId(user.getId())
            .stream()
            .map(CartItemMapper::toCartItemDto)
            .toList();
    }

    
}
