package com.cedriccampagne.ecommerce.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cedriccampagne.ecommerce.auth.dto.AuthResponse;
import com.cedriccampagne.ecommerce.auth.dto.RegisterRequest;
import com.cedriccampagne.ecommerce.auth.dto.LoginRequest;

import com.cedriccampagne.ecommerce.user.User;
import com.cedriccampagne.ecommerce.user.UserRepository;

@Service
public class AuthService  {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            return new AuthResponse("Email déjà utilisé");
        }

        User user = User.builder()
            .username(request.username())
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .build();

        userRepository.save(user);

        return new AuthResponse("Utilisateur créé avec succès : " + user.getUsername());
    }

    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(null);

        if(user == null) {
            return new AuthResponse("Identifiants invalides");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            return new AuthResponse("Identifiants invalides");
        }

        return new AuthResponse("Connexion réussie : " + user.getUsername());
    }
}
