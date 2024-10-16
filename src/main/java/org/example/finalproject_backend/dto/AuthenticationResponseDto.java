package org.example.finalproject_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponseDto {
    private String jwtToken;

    public AuthenticationResponseDto(String jwtToken) {
        this.jwtToken = jwtToken;
    }

}