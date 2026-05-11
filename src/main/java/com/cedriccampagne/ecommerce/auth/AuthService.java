package com.cedriccampagne.ecommerce.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cedriccampagne.ecommerce.auth.dto.AuthResponse;
import com.cedriccampagne.ecommerce.auth.dto.RegisterRequest;
import com.cedriccampagne.ecommerce.security.JwtService;
import com.cedriccampagne.ecommerce.auth.dto.LoginRequest;
import com.cedriccampagne.ecommerce.auth.dto.MeRespone;
import com.cedriccampagne.ecommerce.user.User;
import com.cedriccampagne.ecommerce.user.UserRepository;

@Service
public class AuthService  {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;



    public AuthService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        AuthenticationManager authenticationManager
        
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            return new AuthResponse(null, null, null);
        }

        User user = User.builder()
            .username(request.username())
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .build();

        userRepository.save(user);


        return new AuthResponse(
            null,
            null,
            null
        );
    }

    public AuthResponse login(LoginRequest request){
        
        //1 Laisser Spring vérifier email + password
        authenticationManager.authenticate(
            //objet qui représente une tentative d’authentification
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        // 2) Si on arrive ici = email + password sont corrects
        // On charge l'utilisateur depuis la base
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(()-> new RuntimeException("Utilisateur introuvable"));

        String token = jwtService.generateToken(user);

        return new AuthResponse(
            token,
            user.getEmail(),
            user.getRole()
        );
    }

    public MeRespone me(){
        // var laisse JAVA déterminer automatiquement le type excate de la variable
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getPrincipal() == "anonymusUser") {
            throw new RuntimeException("Utilisateur non authentifié");
        }

        // cast User : Je sais que c’est un User, donc je le cast.
        User user = (User) authentication.getPrincipal();

        return new MeRespone(
            user.getEmail(),
            user.getRole()
        );
    }
}
