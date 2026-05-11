# CustomUserDetails

Objectif : séparer ton domaine (User) de Spring Security.

On va introduire :
- CustomUserDetails
    - wrappe ton User
    - implémente UserDetails
- CustomUserDetailsService
    - implémente UserDetailsService
    - charge un User depuis la base
    - le transforme en CustomUserDetails
- JwtAuthFilter
    - utilise UserDetailsService au lieu de UserRepository
- SecurityConfig  
    - branche le filtre dans la chaîne

## 1. Créer ``CustomUserDetails``

Concept :  C’est une classe qui représente un utilisateur pour Spring Security, à partir de ton User métier.
- Elle implémente UserDetails
- Elle expose :
    - getUsername() => email
    - getPassword() => password
    - getAuthorities() => rôle(s)
    - et quelques booléens (compte expiré, verrouillé, etc.)

```java
package com.cedriccampagne.ecommerce.security;

import com.cedriccampagne.ecommerce.user.User;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
    
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    //collection de n’importe quel type qui hérite de GrantedAuthority.
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public String getPassword(){
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // simple pour ton projet
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // simple
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // simple
    }

    @Override
    public boolean isEnabled() {
        return true; // simple
    }

    public User getUser() {
        return user;
    }
}
```

## 2. Créer ``CustomUserDetailsService``

Concept : C’est la classe que Spring Security utilise pour charger un utilisateur à partir d’un identifiant (email dans ce projet).

Elle :
- implémente ``UserDetailsService``
- injecte ``UserRepository``
- fait ``findByEmail``
- renvoie un ``CustomUserDetails``

```java
package com.cedriccampagne.ecommerce.security;

import com.cedriccampagne.ecommerce.user.User;
import com.cedriccampagne.ecommerce.user.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;

    public CustomDetailsService(UserRepository userRepository) {
       this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + email));

        return new CustomUserDetails(user);
    }
}
```

## 3. Adapter JwtAuthFilter à la version PRO

Le filtre ne va plus parler directement à ``UserRepository``, mais à ``UserDetailsService``.

Concept :
Lire le header ``Authorization``
Extraire le token
Extraire l’email via ``JwtService``
Charger ``UserDetails`` via ``CustomUserDetailsService``
Valider le token
Créer un ``UsernamePasswordAuthenticationToken``
Le mettre dans le ``SecurityContext``

```java
package com.cedriccampagne.ecommerce.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String userEmail = jwtService.extractEmail(token);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.validateToken(token)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
```

### WebAuthenticationDetailsSource

classe Spring Security qui sert à extraire des informations supplémentaires sur la requête HTTP, par exemple :
- l’adresse IP du client
- le User-Agent (navigateur)
- la session ID (si tu étais en mode session)

Elle construit un objet WebAuthenticationDetails.

| Élément | Rôle |
|--------|------|
| WebAuthenticationDetailsSource | Extrait des infos de la requête (IP, user‑agent…) |
| buildDetails(request)| Construit un objet contenant les détails de la requête |
| authToken.setDetails(...)| Ajoute ces infos dans l’objet d’authentification |
| Obligatoire ? |  Non |
| Recommandé ? |  Oui (projets pros) |


## 4. ``SecurityConfig`` qui branche tout ça

Dernière brique : dire à Spring Security :
- quelles routes sont publiques
- quelles routes sont protégées
- quel filtre JWT utiliser
- que tu es en stateless (pas de session)

