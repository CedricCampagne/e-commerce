# @Transactional

## 1. Ça garantit qu’une méthode s’exécute dans UNE SEULE transaction
Tout ce qui se passe dans la méthode est traité comme un bloc indivisible :

soit tout réussit → commit

soit une erreur arrive → rollback

**Pas d’état partiellement sauvegardé.**

## 2. Sans transaction, certains appels JPA sont interdits

Notamment :
- delete()
- remove()
- flush()
- certaines opérations de cascade

D’où ton erreur :

```bash
No EntityManager with actual transaction available
```

## 3. Spring ouvre et ferme la transaction automatiquement

Quand tu mets :

```java
@Transactional
public void createOrderFromCart(...) { ... }
```

Spring fait :

```bash
BEGIN
   insert order
   insert order_items
   update order
   delete cart_items
COMMIT
```

- Tout est cohérent
- Pas d’erreur Hibernate
- Pas de données incohérentes

## 4. Quand l’utiliser ?

Tu mets @Transactional sur :
- les méthodes **service** qui font plusieurs opérations JPA
- les méthodes qui font **save + delete**
- les méthodes qui modifient plusieurs entités liées

**Exactement le cas avec Order + OrderItem + CartItem.**

5. Où le mettre ?

Deux options :
- Sur la méthode (recommandé)

    ```java
    @Transactional
    public OrderDto createOrderFromCart(Long userId) { ... }
    ```

- Ou sur la classe entière

    ```java
    @Service
    @Transactional
    public class OrderService { ... }
    ```

## Résumé ultra-court

``@Transactional`` = une seule transaction pour toute la méthode.  
Obligatoire quand tu fais plusieurs opérations JPA (save + delete).  
Sans ça → erreurs Hibernate et incohérences.  