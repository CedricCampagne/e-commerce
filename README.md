# Projet E‑Commerce API – Spring Boot / PostgreSQL / Flyway

API backend construite avec Spring Boot, PostgreSQL, Flyway et une architecture propre en couches (Entity → Repository → Service → Controller).
Objectif : créer une base solide pour un projet e‑commerce complet.

## Installation & Lancement du projet

### Prérequis

Avant de lancer le projet, assure‑toi d’avoir installé :
- Java 17+
- Maven 3.8+
- PostgreSQL 14+
- Un IDE (IntelliJ, VS Code, Eclipse…)

### 1. Cloner le projet

```bash
git clone https://github.com/<ton-user>/<ton-repo>.git
cd <ton-repo>
```

### 2. Créer le fichier ``.env``

Le fichier ``.env`` n’est pas versionné (présent dans .gitignore).

Crée‑le à la racine du projet :

```bash
DB_URL=jdbc:postgresql://localhost:5432/ecommerce
DB_USER=postgres
DB_PASSWORD=ton_mot_de_passe
```

### 3. Créer la base PostgreSQL

Dans PgAdmin ou en ligne de commande :

```sql
CREATE DATABASE ecommerce;
```

Aucune table à créer manuellement :
- Flyway s’en charge automatiquement au démarrage.

## 4. Vérifier la configuration Spring Boot

Le fichier application.yml doit contenir :

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USER}
    password: ${DB_PASSWORD}

  jpa:
    hibernate:
      ddl-auto: none
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
    show-sql: true

  flyway:
    enabled: true
    locations: classpath:db/migration
```

## 5. Lancer le projet

Avec Maven :

```bash
mvn spring-boot:run
```

Ou depuis ton IDE (Run → DemoApplication).

## 6. Vérification Flyway

Au premier lancement :
- Flyway lit les migrations dans ``src/main/resources/db/migration``
- Exécute les fichiers ``V1__...sql`` à ``V6__...sql``
- Crée automatiquement toutes les tables
- Ajoute l’historique dans flyway_schema_history

Tu peux vérifier dans PgAdmin que les tables sont bien créées.

## Stack Technique
- Java 17+
- Spring Boot
    - Spring Web
    - Spring Data JPA
- PostgreSQL
- Flyway (migrations SQL versionnées)
- Lombok
- Maven

## Base de données & Migrations
Le projet utilise Flyway pour gérer la structure de la base.

- Migrations disponibles
    - V1__create_users.sql
    - V2__create_categories.sql
    - V3__create_products.sql
    - V4__create_cart_items.sql
    - V5__create_orders.sql
    - V6__create_order_items.sql

À chaque démarrage, Flyway vérifie la table :

```bash
flyway_schema_history
```

et exécute automatiquement les migrations manquantes.

## Variables d’environnement

Le projet utilise un fichier ``.env`` (non commité) pour stocker les secrets.

Exemple :

```bash
DB_URL=jdbc:postgresql://localhost:5432/ecommerce
DB_USER=postgres
DB_PASSWORD=motdepasse
```

Chargement automatique dans DemoApplication.java.

## Configuration Spring Boot

Extrait du ``application.yml`` :

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USER}
    password: ${DB_PASSWORD}

  jpa:
    hibernate:
      ddl-auto: none
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
    show-sql: true

  flyway:
    enabled: true
    locations: classpath:db/migration
```

## Architecture du projet

```bash
src/
 └── main/
     ├── java/com.example.demo/
     │    ├── entity/
     │    ├── repository/
     │    ├── service/
     │    └── controller/
     └── resources/
          ├── application.yml
          └── db/migration/
```

## État actuel du projet

- [x] Configuration PostgreSQL

- [x] Intégration Flyway

- [x] Migrations V1 → V6

- [x] Chargement des variables d’environnement

- [ ] Entité User

- [ ] Entité Category

- [ ] Entité Product

- [ ] Services & Controllers

- [ ] Authentification JWT

- [ ] Documentation API (Swagger)

## À propos du développeur

Projet réalisé dans le cadre de ma montée en compétences backend après une formation **DWWM**.
Objectif : renforcer mes compétences Java/Spring pour mes futures candidatures.

