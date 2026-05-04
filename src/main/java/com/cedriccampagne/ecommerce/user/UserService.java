package com.cedriccampagne.ecommerce.user;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cedriccampagne.ecommerce.user.dto.UserCreateDto;
import com.cedriccampagne.ecommerce.user.dto.UserDto;
import com.cedriccampagne.ecommerce.user.dto.UserUpdateDto;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(UserMapper::toUserDto)
            .toList();
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Utilisateur introuvable"
        ));

        return UserMapper.toUserDto(user);
    }

    public UserDto createUser(UserCreateDto dto){
        User user = UserMapper.toEntity(dto);

        User saved = userRepository.save(user);

        return UserMapper.toUserDto(saved);
    }

    public UserDto updateUser(Long id,UserUpdateDto dto){
        User user = userRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("Utilisateur introuvable"));

        UserMapper.updateEntity(user, dto);

        User saved = userRepository.save(user);

        return UserMapper.toUserDto(saved);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur introuvable");
        }

        userRepository.deleteById(id);
    }

}
