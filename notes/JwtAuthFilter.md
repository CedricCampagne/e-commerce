# JwtAuthFilter

## Le rôle du JwtAuthFilter dans Spring Security

JwtService sait :
- générer un token
- valider un token
- extraire l’email
- extraire le rôle

Mais rien ne l’utilise encore dans le pipeline de sécurité.
- C’est le rôle du **JwtAuthFilter**.

Le filtre :
- intercepte chaque requête HTTP
- regarde si un token JWT est présent dans le header
- valide ce token
-extrait l’email
- charge l’utilisateur depuis la base
-crée un objet d’authentification Spring
- le place dans le SecurityContext
- laisse la requête continuer

Sans ce filtre => Spring ne sait pas que tu es connecté, même si ton token est valide.

## Où se place le JwtAuthFilter dans la chaîne (Spring Security Filter Chain) ?

Il se place avant le filtre standard ``UsernamePasswordAuthenticationFilter``:
- ``UsernamePasswordAuthenticationFilter`` gère login/password
- on veut gérer JWT
- donc ton filtre doit passer avant, pour dire à Spring : “Cet utilisateur est déjà authentifié via token.”

## Les étapes internes du JwtAuthFilter

### 1. Intercepter la requête

Le filtre reçoit :
- la requête HTTP
- la réponse
- la chaîne de filtres suivante

Il doit décider : “Est-ce que je traite cette requête ou je la laisse passer ?”

### 2. Lire le header Authorization

Il regarde :

```bash
String authHeader = request.getHeader("Authorization");
```

Si :
- pas de header
- header ne commence pas par Bearer

Le filtre ne fait rien  
Il laisse passer la requête  
L’utilisateur reste anonyme  

### 3. Extraire le token

Il récupère la partie après Bearer.

### 4. Valider le token

Il appelle ton JwtService.validateToken(token).

Si :
- token expiré
-signature invalide
- token corrompu

Le filtre ne met rien dans le SecurityContext    
La requête continue comme anonyme  

### 5. Extraire l’email

Il appelle :

```java
extractEmail(token)
```

C’est l’identifiant de l’utilisateur.

### 6. Charger l’utilisateur depuis la base

Il appelle ton UserDetailsService :

Pourquoi ?

Parce que Spring Security veut :
- les rôles depuis la base
- l’état du compte (enabled, locked…)
- les authorities

Même si le token contient un rôle, **Spring ne l’utilise pas**.
Il recharge toujours l’utilisateur depuis la base.

### 7. Créer un objet Authentication

Il crée un :

```java
UsernamePasswordAuthenticationToken
```

avec :
- l’utilisateur
- null (pas de password)
- les authorities (rôles)

### 8. Mettre l’authentification dans le SecurityContext

```java
SecurityContextHolder.getContext().setAuthentication(authentication);
```

À partir de ce moment :

- Spring considère l’utilisateur comme connecté  
- Les contrôleurs peuvent utiliser @AuthenticationPrincipal  
- Les routes protégées fonctionnent
- Les rôles sont appliqués

### 9. Continuer la chaîne

Le filtre laisse la requête continuer :

```java
filterChain.doFilter(request, response);
```

## Résumé ultra‑clair

Le JwtAuthFilter :
- intercepte chaque requête
- vérifie la présence d’un token
- valide le token
- extrait l’email
- charge l’utilisateur depuis la base
- crée une authentification Spring
- la place dans le SecurityContext
- laisse la requête continuer

Sans ce filtre => le JWT ne sert à rien.