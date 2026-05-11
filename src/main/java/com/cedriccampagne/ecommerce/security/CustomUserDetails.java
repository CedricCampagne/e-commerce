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
