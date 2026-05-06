# Les 9 étapes métier de createOrderFromCart(userId)

1. Vérifier que l’utilisateur existe

- On récupère l’utilisateur via userRepository.findById(userId)
- Si l’utilisateur n’existe pas => exception

**C’est la base de toute opération liée au panier ou aux commandes**.

2. Récupérer tous les CartItems de l’utilisateur

- Via cartItemRepository.findByUserId(userId)
- Si le panier est vide => erreur “Panier vide”

**Impossible de créer une commande sans panier**.

3. Calculer le total de la commande

- Pour chaque CartItem :

    ```java
    lineTotal = product.price * quantity
    ```

- Puis :

    ```java
    orderTotal = somme de tous les lineTotal
    ```

**Ce total sera figé dans l’entité Order**.

4. Créer l’entité Order (sans items pour l’instant)

On crée un nouvel Order :
user = l’utilisateur
totalPrice = total calculé
createdAt = maintenant
items = liste vide (pour l’instant)

**On sauvegarde l’Order pour obtenir son ID**.

5. Créer les OrderItem (une ligne par CartItem)

Pour chaque CartItem :
- product = cartItem.product
- quantity = cartItem.quantity
- priceAtPurchase = product.price (figé)
- order = l’Order créé juste avant

**Chaque OrderItem est une photo du panier au moment de l’achat**.

6. Associer les OrderItem à l’Order

- Une fois tous les OrderItem créés :
- on les ajoute à order.getItems()
- on resauvegarde l’Order (cascade ALL gère les items)

**L’Order devient complet**.

7. Vider le panier

Après la création de la commande :
- supprimer tous les CartItems de l’utilisateur

**Le panier doit être vide après une commande**.

8. Retourner un OrderDto complet

Grâce à OrderMapper :
- id
- userId
- totalPrice
- createdAt
- items (OrderItemDto)

**Le front reçoit une commande complète prête à afficher**.

## Résumé ultra‑clair


1. Vérifier user
2. Récupérer panier
3. Calculer total
4. Créer Order (sans items)
5. Créer OrderItems
6. Associer OrderItems à Order
7. Vider panier
8. Retourner OrderDto

C’est le workflow standard d’un e‑commerce professionnel.



## Code

```java
// Créer une commande a partir d'un panier par utilisateur
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
        return OrderMapper.toOrderdto(finalOrder);
    }
```