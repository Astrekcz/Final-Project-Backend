package org.example.finalproject_backend;

import org.example.finalproject_backend.entity.Book;
import org.example.finalproject_backend.entity.Review;
import org.example.finalproject_backend.entity.User;
import org.example.finalproject_backend.repository.BookRepository;
import org.example.finalproject_backend.repository.ReviewRepository;
import org.example.finalproject_backend.repository.UserRepository;
import org.example.finalproject_backend.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void testSaveReviewWhenRatingOutOfRange() {
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                reviewService.saveReview("Great book!", 11, 1L)
        );
        assertEquals("rating out of range", exception.getMessage());
    }

    @Test
    void testSaveReviewWhenBookNotFound() {
        // Given
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                reviewService.saveReview("Great book!", 5, 1L)
        );
        assertEquals("book not found", exception.getMessage());
    }

    @Test
    void testSaveReviewWhenUserNotFound() {
        // Given
        Book book = new Book(); // assuming this is a simple POJO
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        // Mock Security Context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user@example.com");
        SecurityContextHolder.setContext(securityContext);

        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(mock(UserDetails.class));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                reviewService.saveReview("Great book!", 5, 1L)
        );

    }
    @Test
    void testSaveReviewWhenDuplicateReviewExists() {
        // Given
        Book book = new Book();
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        User user = new User();
        Review existingReview = new Review();
        existingReview.setBook(book);
        user.setReviews(List.of(existingReview));

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user@example.com");
        SecurityContextHolder.setContext(securityContext);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                reviewService.saveReview("Great book!", 5, 1L)
        );
        //assertEquals("Book is already reviewed by user", exception.getMessage());
    }




}



