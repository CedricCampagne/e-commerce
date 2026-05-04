package com.cedriccampagne.ecommerce.user;

import com.cedriccampagne.ecommerce.user.dto.UserCreateDto;
import com.cedriccampagne.ecommerce.user.dto.UserDto;
import com.cedriccampagne.ecommerce.user.dto.UserUpdateDto;

public class UserMapper {
    
    public static UserDto toUserDto(User user) {
        return  new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole()
        );
    }

    public static User toEntity(UserCreateDto dto) {
        return User.builder()
            .username((dto.username()))
            .email(dto.email())
            .password(dto.password())
            .role("USER")
            .build();
    }

    public static void updateEntity(User user, UserUpdateDto dto) {
        if (dto.username() != null && !dto.username().isBlank()) {
            user.setUsername(dto.username());
        }

        if (dto.email() != null && !dto.email().isBlank()) {
            user.setEmail(dto.email());
        }

        if(dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(dto.password());
        }
    }
}
