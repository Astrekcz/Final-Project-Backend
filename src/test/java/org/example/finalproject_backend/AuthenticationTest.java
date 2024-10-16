package org.example.finalproject_backend;



import org.example.finalproject_backend.dto.AuthenticationRequestDto;
import org.example.finalproject_backend.dto.AuthenticationResponseDto;
import org.example.finalproject_backend.dto.UserRegisterDto;
import org.example.finalproject_backend.entity.Role;
import org.example.finalproject_backend.entity.User;
import org.example.finalproject_backend.repository.UserRepository;
import org.example.finalproject_backend.service.AuthService;
import org.example.finalproject_backend.service.CustomUserDetailsService;
import org.example.finalproject_backend.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @InjectMocks
    private AuthService authenticationService;



    @Test
    public void testUserRegistration() {
        UserRegisterDto registerRequest = new UserRegisterDto();

        registerRequest.setFirstName("Pepa");
        registerRequest.setLastName("Skorica");
        registerRequest.setPassword(("p=4TT&uxr+–2<.–"));
        registerRequest.setRole(Role.USER);
        registerRequest.setEmail("Skorica@gmail.com");


        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");


        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already taken");
        }
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();


        assertEquals(registerRequest.getFirstName(), user.getFirstName() );
        assertEquals(registerRequest.getLastName(), user.getLastName());
        assertEquals(registerRequest.getEmail(), user.getEmail());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(registerRequest.getRole(), user.getRole());

    }

    @Test
    public void RegisterAsAdminTest(){
        UserRegisterDto registerRequest = new UserRegisterDto();

        registerRequest.setFirstName("Pepek");
        registerRequest.setLastName("Vyskoc");
        registerRequest.setPassword(("p=4TT&uxr+–2<.–v"));
        registerRequest.setRole(Role.ADMIN);
        registerRequest.setEmail("Vyskoc@gmail.com");


        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");


        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already taken");
        }
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.ADMIN)
                .build();

        userRepository.save(user);

        assertEquals(registerRequest.getFirstName(), user.getFirstName() );
        assertEquals(registerRequest.getLastName(), user.getLastName());
        assertEquals(registerRequest.getEmail(), user.getEmail());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(registerRequest.getRole(), user.getRole());

    }



    @Test
    public void TestLoginSuccess(){
        // Arrange
        String email = "Vyskoc@gmail.com";
        String rawPassword = "p=4TT&uxr+–2<.–v";
        String encodedPassword = "encodedPassword";
        String jwtToken = "mockedJwtToken";

        // Prepare request and user objects
        AuthenticationRequestDto request = new AuthenticationRequestDto();
        request.setEmail(email);
        request.setPassword(rawPassword);

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .build();


        // Mocking dependencies
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails, user.getUserId())).thenReturn(jwtToken);

        // Act
        AuthenticationResponseDto response = authenticationService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals(jwtToken, response.getJwtToken());

        // Verify interactions
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(rawPassword, encodedPassword);
        verify(userDetailsService).loadUserByUsername(email);
        verify(jwtUtil).generateToken(userDetails, user.getUserId());
    }


    @Test
    public void testLogin_InvalidCredentials() {
        // Arrange
        String email = "Vyskoc@gmail.com";
        String rawPassword = "p=4TT&uxr+–2<.–v";
        String encodedPassword = "encodedPassword";

        // Prepare request and user objects
        AuthenticationRequestDto request = new AuthenticationRequestDto();
        request.setEmail(email);
        request.setPassword(rawPassword);

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .build();

        // Mocking dependencies
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationService.login(request);
        });

        assertEquals("Invalid credentials", exception.getMessage());

        // Verify interactions
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(rawPassword, encodedPassword);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtUtil, never()).generateToken(any(UserDetails.class), anyLong());
    }
    @Test
    public void testLogin_UserNotFound() {
        // Arrange
        String email = "nonexistent@gmail.com";
        String rawPassword = "somePassword";

        // Prepare request
        AuthenticationRequestDto request = new AuthenticationRequestDto();
        request.setEmail(email);
        request.setPassword(rawPassword);

        // Mocking dependencies
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticationService.login(request);
        });

        assertEquals("User not found", exception.getMessage());

        // Verify interactions
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtUtil, never()).generateToken(any(UserDetails.class), anyLong());
    }



}
