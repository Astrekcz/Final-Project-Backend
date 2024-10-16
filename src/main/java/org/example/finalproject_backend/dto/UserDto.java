package org.example.finalproject_backend.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.finalproject_backend.entity.Role;
@Builder
@Getter
@Setter
public class UserDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;

}