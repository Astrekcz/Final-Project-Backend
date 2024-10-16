package org.example.finalproject_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.finalproject_backend.entity.Book;
import org.example.finalproject_backend.entity.User;
import org.example.finalproject_backend.entity.Review;
import org.example.finalproject_backend.repository.BookRepository;
import org.example.finalproject_backend.repository.ReviewRepository;
import org.example.finalproject_backend.repository.UserRepository;
import org.example.finalproject_backend.util.JwtUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    //    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasRole('USER')")
    public void saveReview(String text, int rating, Long bookId) {
        if (rating < 1 || rating > 10) {
            throw new RuntimeException("rating out of range");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("book not found"));

        // Retrieve the username from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // this is email address

        // Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));


        List<Review> listOfUserReviews = user.getReviews();
        for (Review review : listOfUserReviews) {
            if (review.getBook().getBookId().equals(bookId)) {
                throw new RuntimeException("Book is already reviewed by user");
            }
        }
        Review review = Review.builder()
                .text(text)
                .rating(rating)
                .build();
        review.setBook(book);
        review.setUser(user);
        reviewRepository.save(review);
        updateOverallRatingAndAdjustNumberOfReviews(book);
    }

    public void updateOverallRatingAndAdjustNumberOfReviews(Book book) {
        book.adjustNumberOfReviews();

        List<Review> reviews = book.getReviews();
        if (reviews.isEmpty()) {
            book.setOverallRating(0.0);
        } else {
            double sumOfRating = 0;
            for (Review review : reviews) {
                sumOfRating += review.getRating();
            }
            double updatedRanking = sumOfRating / reviews.size();
            book.setOverallRating(updatedRanking);
        }
        bookRepository.save(book);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteReviewAsAdmin(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("review not found"));
        Book book = review.getBook();
        reviewRepository.delete(review);
        updateOverallRatingAndAdjustNumberOfReviews(book);

    }

    @PreAuthorize("hasRole('USER')")
    public void deleteReviewAsUser(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Retrieve the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("Authentication object is null");
        }

        // Extract user details from the authentication object
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername(); // this is email address

        // Log username for debugging purposes
        log.debug("Username: {}", username);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));


        // Check if the review belongs to the logged-in user
        if (!review.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You can only delete your own reviews");
        }
        Book book = review.getBook();
        reviewRepository.delete(review);
        updateOverallRatingAndAdjustNumberOfReviews(book);
    }

    @PreAuthorize("hasRole('USER')")
    public void updateReviewAsUser(Long reviewId, String newText, int newRating) {
        if (newRating < 1 || newRating > 10) {
            throw new IllegalArgumentException("Rating must be between 1 and 10");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Retrieve the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("Authentication object is null");
        }

        // Extract user details from the authentication object
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername(); // this is email address

        // Log username for debugging purposes
        log.debug("Username: {}", username);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the review belongs to the logged-in user
        if (!review.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You can only edit your own reviews");
        }

        review.setText(newText);
        review.setRating(newRating);

        reviewRepository.save(review);
        updateOverallRatingAndAdjustNumberOfReviews(review.getBook());
    }


    @PreAuthorize("hasRole('USER')")
    public List<Review> getUserReviews() {
        // Retrieve the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("Authentication object is null");
        }

        // Extract user details from the authentication object
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername(); // this is email address

        // Log username for debugging purposes
        log.debug("Username: {}", username);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return reviewRepository.findAllByUserUserId(user.getUserId());
    }

    public List<Review> getReviewsByBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("book not found"));
        List<Review> reviewListByBook = book.getReviews();
        return reviewListByBook;
    }

    @PreAuthorize("hasRole('USER')")
    public Review getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Retrieve the username from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // this is email address

        // Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Review> usersReviews = user.getReviews();
        for (Review userReview : usersReviews) {
            if (userReview.getReviewId().equals(reviewId)) {
                return review;
            }
        }
        throw new RuntimeException("User does not have reviews with specific Id");
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }
}
