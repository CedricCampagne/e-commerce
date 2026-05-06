# E‑Commerce API – Spring Boot / PostgreSQL / Flyway

API backend e‑commerce construite avec Spring Boot, PostgreSQL et Flyway.  
Le projet suit une architecture professionnelle en couches (Entity => Repository => Service => Controller)   
et implémente les fonctionnalités essentielles d’un vrai backend e‑commerce.

Objectif : **monter en compétences backend Java/Spring** et produire un projet propre, structuré et réaliste pour mes futures candidatures.

## Fonctionnalités

[x] Gestion des utilisateurs

[x] Gestion des catégories

[x] Gestion des produits

[x] Panier utilisateur (CartItem)

[x] Création de commande (Order + OrderItem)

[ ] Authentification JWT

[ ] Documentation Swagger

[ ] Tests unitaires

[ ] Docker / Déploiement

## Endpoints principaux

### Produits

- GET /products
- GET /products/{id}

### Panier

- GET /cart/user/{userId}
- POST /cart/user/{userId}/add
- DELETE /cart/user/{userId}/{productId}

### Commandes

- POST /orders/user/{userId} => crée une commande à partir du panier
- GET /orders/{id}
- GET /orders/user/{userId}

## Architecture du projet

```bash
src/
 └── main/
     ├── java/com.cedriccampagne.ecommerce/
     │    ├── user/
     │    ├── category/
     │    ├── product/
     │    ├── cartItem/
     │    ├── order/
     │    ├── orderItem/
     │    ├── dto/
     │    ├── mapper/
     │    └── config/
     └── resources/
          ├── application.yml
          └── db/migration/
```

### Modules

- User

Gestion des comptes utilisateurs (email, password hash, rôle).

- Category

Catégories de produits.

- Product

Catalogue produits (nom, description, prix, stock…).

- CartItem

Représente une ligne du panier.  
Pas d’entité “Cart” => le panier = liste de CartItem par user.

- Order / OrderItem

Snapshot du panier au moment de l’achat :
  - Order = commande
  - OrderItem = lignes de commande figées (prix au moment de l’achat)

## Stack technique

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Flyway (migrations SQL versionnées)
- Lombok
- Maven

## Roadmap

[x] Panier

[x] Commandes

[ ] Authentification JWT

[ ] Swagger / OpenAPI

[ ] Tests unitaires

[ ] Docker

[ ] Déploiement (Railway / Render / VPS)

## Installation rapide

1. Cloner le projet

```bash
git clone https://github.com/<user>/<repo>.git
cd <repo>
```

2. Créer un fichier .env à la racine

```bash
DB_URL=jdbc:postgresql://localhost:5432/ecommerce
DB_USER=postgres
DB_PASSWORD=motdepasse
```

3. Créer la base PostgreSQL

```sql
CREATE DATABASE ecommerce;
```

4. Lancer l’application

```bash
mvn spring-boot:run
```

Flyway crée automatiquement toutes les tables au démarrage.

## À propos

Projet réalisé dans le cadre de ma montée en compétences backend après une formation DWWM.  
Objectif : viser un poste développeur backend Java junior en maîtrisant Spring Boot, PostgreSQL, Flyway et les bonnes pratiques d’architecture.  