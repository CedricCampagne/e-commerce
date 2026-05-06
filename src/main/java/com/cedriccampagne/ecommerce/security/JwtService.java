package com.cedriccampagne.ecommerce.security;

import org.springframework.stereotype.Service;

import com.cedriccampagne.ecommerce.user.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;

@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        //lit cette chaîne
        //la décode en tableau de bytes byte[] ces bytes représentent la vraie clé HMAC que tu as générée avec openssl rand -base64 32.
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(User user){
        return Jwts.builder()
            .setSubject(user.getEmail())
            .claim("role", user.getRole())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
            // signWith avec JJWT 0.11.x et sup
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public String extractRole(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        // "role =>"Récupère la valeur brute (type Object)
        // String.class	=> Récupère la valeur en String, proprement et en sécurité
        .get("role", String.class);
    }
}
