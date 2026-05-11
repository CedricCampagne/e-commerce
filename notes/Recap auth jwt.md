# Récap Auth avec jwt token

Un système d’authentification PRO avec JWT repose sur 4 briques :
- ``UserDetails`` => identité
- ``AuthenticationManager`` => vérification
- ``JwtService`` => création/lecture du token
- ``JwtAuthFilter`` => sécurisation des requêtes

## 1. Le cœur du système : l’utilisateur (User)

L'utilisateur doit être capable d’être utilisé par Spring Security.

Il doit fournir :
email (identifiant)
password (hashé)
rôle
authorities (dérivées du rôle)

C’est le rôle de ``UserDetails``.

**Pourquoi ?**   

Parce que Spring Security ne travaille qu’avec des UserDetails, pas avec tes entités brutes.

## 2. Le moteur d’authentification : ``AuthenticationManager``

C’est lui qui fait le travail critique :
charger l’utilisateur via ``UserDetailsService``
comparer le mot de passe fourni avec le mot de passe hashé
vérifier que le compte n’est pas désactivé
vérifier que le compte n’est pas expiré

On lui donne :

```java
new UsernamePasswordAuthenticationToken(email, password)
```

Et lui dit :
- OK => utilisateur authentifié
- KO => exception

**C’est la brique PRO qui remplace les vérifications manuelles.**

## 3. Le ``JwtService`` : fabriquer et lire les tokens

Il a 3 responsabilités :
- Générer un token
    - subject = email
    - claims = rôle
    - expiration
    - signature HMAC SHA‑256

- Extraire les infos
    - email
    - rôle
    -expiration

- Valider un token (version PRO)
    - C’est ici que tu utilises isTokenValid :
        - le token appartient-il à cet utilisateur ?
        - le token n’est-il pas expiré ?

**C’est la validation PRO**, pas juste la signature est bonne.

## 4. Le ``JwtAuthFilter`` : sécuriser toutes les requêtes
C’est le garde du corps de ton API.

Il fait :
- lire le header Authorization
- vérifier “Bearer …”
- extraire le token
- extraire l’email
- vérifier que personne n’est déjà authentifié
- charger l’utilisateur depuis la base
- valider le token avec ``isTokenValid()``
- créer un Authentication
- le mettre dans le SecurityContext
- laisser passer la requête

**C’est lui qui transforme un JWT en utilisateur authentifié.**

## 5. ``SecurityConfig`` : assembler le système

Elle fait 4 choses :

- Exposer ``AuthenticationManager``
    Pour que le AuthService puisse l’utiliser.

- Déclarer ``PasswordEncoder``
    Pour encoder les mots de passe.

- Définir les règles d’accès
    - /auth/** → public
    - tout le reste → protégé

- Ajouter ton ``JwtAuthFilter``
    Avant ``UsernamePasswordAuthenticationFilter``.

**C’est la glue qui assemble tout.**

## 6. Le flux complet d’un système PRO (du login à la requête sécurisée)

- Login
    - Front envoie email + password
    - AuthService appelle AuthenticationManager
    - Spring vérifie le mot de passe
    - AuthService génère un JWT
    - Front stocke le JWT

**Aucune session, tout est stateless**.

- Requête sécurisée
    - Front envoie ``Authorization: Bearer <token>``
    - JwtAuthFilter lit le token
    - JwtService extrait l’email
    - JwtAuthFilter charge l’utilisateur
    - JwtService valide le token
    - JwtAuthFilter authentifie la requête
    - Le controller peut accéder à l’utilisateur

**Le JWT remplace totalement la session.**

## 7. Pourquoi cette architecture est PRO ?

Parce qu’elle :
- utilise les mécanismes internes de Spring Security
- sépare les responsabilités
- évite les vérifications manuelles
- est stateless (scalable, moderne)
- est compatible avec Angular, React, mobile, etc.
- est lisible et maintenable

**C’est l’architecture utilisée dans les vraies API professionnelles.**

## 8. Résumé (format ultra clair)

- User (entité)
   - email, password, role
   - implémente UserDetails

- UserDetailsService
   - charge l’utilisateur par email

- AuthenticationManager
   - vérifie email + password
   - utilisé dans AuthService.login()

- JwtService
   - generateToken()
   - extractEmail()
   - extractRole()
   - isTokenValid(token, user)

- JwtAuthFilter
   - lit le token
   - extrait email
   - charge user
   - valide token
   - authentifie la requête

- SecurityConfig
   - expose AuthenticationManager
   - expose PasswordEncoder
   - configure les routes
   - ajoute JwtAuthFilter


## Résumé ultra‑clair (ordre exact à suivre)

Voici l’ordre PRO, celui a suivre dans tous mes futurs projets :
- Créer User + UserRepository
- Créer CustomUserDetails
- Créer CustomUserDetailsService
- Configurer AuthenticationManager
- Créer AuthService PRO
- Créer JwtService PRO
- Créer JwtAuthFilter PRO
- Créer SecurityConfig PRO
- Tester avec Postman
- Créer /auth/me (optionnel mais PRO)

**C’est le pipeline PRO.**

**C’est celui que tu dois refaire dans tous tes projets.**
