package org.example.finalproject_backend;

import org.example.finalproject_backend.entity.User;
import org.example.finalproject_backend.repository.UserRepository;
import org.example.finalproject_backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Setup SecurityContext with mock Authentication
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com"); // Simulate email address
    }

    @Test
    public void testDeleteUserAsTheUser() {
        User user = User.builder()
                .userId(1L)
                .email("user@example.com")
                .reviews(Collections.emptyList()) // Ensure userReviews is not null
                .build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        userService.deleteUserAsTheUser();

        verify(userRepository).delete(user);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUserAsAdmin_throwsWhenUserNotFound() {
        // Given
        User adminUser = new User();
        adminUser.setUserId(1L);
        adminUser.setEmail("admin@example.com");

        when(authentication.getPrincipal()).thenReturn(adminUser);
        when(userRepository.findByEmail(adminUser.getEmail())).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> userService.deleteUserAsAdmin(2L));
        verify(userRepository, never()).delete(any(User.class));
    }


}