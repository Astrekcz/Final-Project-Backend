package org.example.finalproject_backend.mapper;

import org.example.finalproject_backend.dto.UserDto;
import org.example.finalproject_backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto userEntityToDto(User user) {
        UserDto userGetUserDto = UserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
        return userGetUserDto;
    }
}
