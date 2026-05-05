# BDD

## Les tables

### 1. Users

| Champ       | Type              | Description                |
|-------------|-------------------|----------------------------|
| id          | BIGSERIAL (PK)    | Identifiant unique         |
| username    | VARCHAR(100)      | Nom d’utilisateur          |
| email       | VARCHAR(255)      | Email unique               |
| password    | VARCHAR(255)      | Mot de passe hashé         |
| role        | VARCHAR(50)       | USER / ADMIN               |
| created_at  | TIMESTAMP         | Date de création           |
| updated_at  | TIMESTAMP         | Dernière mise à jour       

- Relations :
    - 1 user → N orders
    - 1 user → N cart_items


### 2. Table : categories

| Champ       | Type              | Description                |
|-------------|-------------------|----------------------------|
| id          | BIGSERIAL (PK)    | Identifiant unique         |
| name        | VARCHAR(255)      | Nom de la catégorie        |
| created_at  | TIMESTAMP         | Date de création           |
| updated_at  | TIMESTAMP         | Dernière mise à jour       |

- Relations :
    - 1 category → N products

### 3. Table : products

| Champ        | Type              | Description                     |
|--------------|-------------------|---------------------------------|
| id           | BIGSERIAL (PK)    | Identifiant unique              |
| name         | VARCHAR(255)      | Nom du produit                  |
| description  | TEXT              | Description du produit          |
| price        | NUMERIC(10,2)     | Prix                            |
| stock        | INT               | Stock disponible                |
| image_url    | TEXT              | URL de l’image                  |
| category_id  | BIGINT (FK)       | Référence → categories.id       |
| created_at   | TIMESTAMP         | Date de création                |
| updated_at   | TIMESTAMP         | Dernière mise à jour            |

- Relations :
    - N products → 1 category
    - 1 product → N cart_items
    - 1 product → N order_items

### 4. Table : cart_items

| Champ        | Type              | Description                     |
|--------------|-------------------|---------------------------------|
| id           | BIGSERIAL (PK)    | Identifiant unique              |
| user_id      | BIGINT (FK)       | Référence → users.id            |
| product_id   | BIGINT (FK)       | Référence → products.id         |
| quantity     | INT               | Quantité dans le panier         |
| created_at   | TIMESTAMP         | Date de création                |
| updated_at   | TIMESTAMP         | Dernière mise à jour            |

- Relations :
    - 1 user → N-  cart_items
    - 1 product     → N cart_items

CartItem = une ligne du panier

User a 1 panier = liste de CartItems

CartItem = user + product + quantity

Pas de table Cart

### 5. Table : orders

| Champ        | Type              | Description                     |
|--------------|-------------------|---------------------------------|
| id           | BIGSERIAL (PK)    | Identifiant unique              |
| user_id      | BIGINT (FK)       | Référence → users.id            |
| total_price  | NUMERIC(10,2)     | Total payé                      |
| created_at   | TIMESTAMP         | Date de création                |
| updated_at   | TIMESTAMP         | Dernière mise à jour            |

- Relations :
    - 1 user → N orders
    - 1 order → N order_items

### 6. Table : order_items

| Champ             | Type              | Description                          |
|-------------------|-------------------|--------------------------------------|
| id                | BIGSERIAL (PK)    | Identifiant unique                   |
| order_id          | BIGINT (FK)       | Référence → orders.id                |
| product_id        | BIGINT (FK)       | Référence → products.id              |
| quantity          | INT               | Quantité achetée                     |
| price_at_purchase | NUMERIC(10,2)     | Prix au moment de l’achat            |

- Relations :
    - 1 order → N order_items
    - 1 product → N order_items

### Résumé des relations

- User (1) → (N) Orders
- User (1) → (N) CartItems
- Category (1) → (N) Products
- Product (1) → (N) CartItems
- Product (1) → (N) OrderItems
- Order (1) → (N) OrderItems

## Les scripts

Flyway lit les fichiers dans :

````bash
src/main/resources/db/migration/
````

Pour que flyway éxécute les scripts dans l'ordre 
Le format exact est :

````bash
V<version>__<description>.sql
````

Avec deux underscores entre version et description.

### flyway_schema_history

C’est une table créée automatiquement par Flyway dans ta base PostgreSQL.

Elle contient l’historique complet de toutes les migrations exécutées :
- quelle version (V1, V2, V3…)
- quel fichier SQL
- quand ça a été exécuté
- si ça a réussi ou échoué
- le checksum (empreinte) du fichier

En gros : c’est le journal de bord de ta base de données.

Flyway l’utilise pour :

- Savoir quelles migrations ont déjà été appliquées
    Si V1, V2, V3 sont déjà là => Flyway ne les rejoue pas.

- Empêcher de modifier une migration déjà appliquée
    Si tu modifies V1 après exécution => Flyway détecte un checksum différent => erreur.
    C’est normal : on ne réécrit pas l’histoire.

- Exécuter uniquement les nouvelles migrations
    Si tu ajoutes V7 => Flyway voit qu’elle n’est pas dans la table => il l’exécute.

- Garantir que toutes les bases (dev, test, prod) sont synchronisées
    Même historique = même structure.