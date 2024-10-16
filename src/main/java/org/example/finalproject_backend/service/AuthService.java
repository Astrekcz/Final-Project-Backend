package org.example.finalproject_backend.service;

import lombok.RequiredArgsConstructor;
import org.example.finalproject_backend.dto.AuthenticationRequestDto;
import org.example.finalproject_backend.dto.AuthenticationResponseDto;
import org.example.finalproject_backend.dto.UserRegisterDto;
import org.example.finalproject_backend.entity.Role;
import org.example.finalproject_backend.entity.User;
import org.example.finalproject_backend.repository.UserRepository;
import org.example.finalproject_backend.util.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService; // Inject CustomUserDetailsService


    public User register(UserRegisterDto registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already taken"); // Or create a custom exception
        }
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    //backend feature only
    public User registerAsAdmin(UserRegisterDto registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already taken"); // Or create a custom exception
        }
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.ADMIN)
                .build();

        return userRepository.save(user);
    }

    public AuthenticationResponseDto login(AuthenticationRequestDto authenticationRequest) {
        User user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail()); // Load UserDetails
            String jwtToken = jwtUtil.generateToken(userDetails, user.getUserId()); // Generate JWT token with userId
            return new AuthenticationResponseDto(jwtToken);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

}