package org.example.finalproject_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.finalproject_backend.dto.ReviewByUserDto;
import org.example.finalproject_backend.dto.ReviewDto;
import org.example.finalproject_backend.dto.ReviewForAdminDto;
import org.example.finalproject_backend.dto.ReviewPartialDto;
import org.example.finalproject_backend.entity.Review;
import org.example.finalproject_backend.mapper.ReviewMapper;
import org.example.finalproject_backend.repository.ReviewRepository;
import org.example.finalproject_backend.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @PreAuthorize("hasRole('USER')")
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/saveReview")
    public ResponseEntity<String> saveReview(@Valid @RequestBody ReviewPartialDto reviewDto,
                                             @RequestParam Long bookId) {
        try {
            reviewService.saveReview(reviewDto.getText(), reviewDto.getRating(), bookId);
            return new ResponseEntity<>("review was saved", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/deleteReviewAsAdmin")
    public ResponseEntity<String> deleteReviewAsAdmin(@RequestParam Long reviewId) {
        try {
            reviewService.deleteReviewAsAdmin(reviewId);
            return new ResponseEntity<>("review was deleted", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getAllReviewsAsAdmin")
    public ResponseEntity<?> getAllReviewsAsAdmin() {
        try {
            List<Review> reviewListForAdmin = reviewService.getAllReviews();
            List<ReviewForAdminDto> reviewForAdminDto = new ArrayList<>();
            for (Review review:reviewListForAdmin) {
                reviewForAdminDto.add(reviewMapper.reviewForAdminDto(review));
            }
            return new ResponseEntity<>(reviewForAdminDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/deleteReviewAsUser")
    public ResponseEntity<String> deleteReviewAsUser(@RequestParam Long reviewId) {
        try {
            reviewService.deleteReviewAsUser(reviewId);
            return new ResponseEntity<>("review was deleted", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @CrossOrigin(origins = "http://localhost:3000")
    @PatchMapping("/updateReviewAsUser")
    public ResponseEntity<String> updateReviewAsUser(@RequestParam Long reviewId,
                                                     @RequestBody ReviewPartialDto reviewDto) {
        try {
            reviewService.updateReviewAsUser(reviewId, reviewDto.getText(), reviewDto.getRating());
            return new ResponseEntity<>("Review was updated", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getUserReviews")
    public ResponseEntity<List<ReviewByUserDto>> getUserReviews() {
        try {
            List<Review> reviews = reviewService.getUserReviews();
            List<ReviewByUserDto> reviewDtos = new ArrayList<>();
            for (Review review : reviews) {
                reviewDtos.add(reviewMapper.reviewByUserDto(review));
            }
            return new ResponseEntity<>(reviewDtos, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error getting user reviews", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getReviewsByBook")
    public ResponseEntity<?> getReviewsByBook(Long bookId) {
        try {
            List<Review> list = reviewService.getReviewsByBook(bookId);
            List<ReviewDto> reviewDtoList = new ArrayList<>();
            for (Review review : list) {
                reviewDtoList.add(reviewMapper.reviewEntityToDto(review));
            }
            return new ResponseEntity<>(reviewDtoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getReviewById")
    public ResponseEntity<?> getReviewById(@RequestParam Long reviewId) {
        try {
            Review review = reviewService.getReviewById(reviewId);
            ReviewPartialDto reviewPartialDto = reviewMapper.reviewPartialDto(review);
            return new ResponseEntity<>(reviewPartialDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
