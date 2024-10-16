package org.example.finalproject_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.finalproject_backend.entity.Book;
import org.example.finalproject_backend.entity.Review;
import org.example.finalproject_backend.entity.User;
import org.example.finalproject_backend.repository.ReviewRepository;
import org.example.finalproject_backend.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class UserService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;
    private final UserDetailsService CustomUserDetailsService;

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUserAsAdmin(Long userId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("Authentication object is null");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        if (currentUser.getUserId().equals(userId)) {
            throw new RuntimeException("Admin cannot delete themselves");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));

        List<Review> list = user.getReviews();
        userRepository.delete(user);
        for (Review review : list) {
            reviewService.updateOverallRatingAndAdjustNumberOfReviews(review.getBook());
        }
    }


    @PreAuthorize("hasRole('USER')")
    public void deleteUserAsTheUser() {
        User currentUser = getAuthenticatedUser();
        List<Review> userReviews = currentUser.getReviews();

        userRepository.delete(currentUser);

        for (Review review : userReviews) {
            reviewService.updateOverallRatingAndAdjustNumberOfReviews(review.getBook());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new RuntimeException("No authentication or principal found");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    // Optional: method to update user details
    @PreAuthorize("hasRole('USER')")
    public User updateUserDetails(User updatedUser) {
        User currentUser = getAuthenticatedUser();

        currentUser.setFirstName(updatedUser.getFirstName());
        currentUser.setLastName(updatedUser.getLastName());
        currentUser.setEmail(updatedUser.getEmail());

        // Only update password if it's not empty
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            currentUser.setPassword(updatedUser.getPassword());
        }

        return userRepository.save(currentUser);
    }
}

