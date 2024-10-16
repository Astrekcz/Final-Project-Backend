package org.example.finalproject_backend.mapper;

import org.example.finalproject_backend.dto.*;
import org.example.finalproject_backend.entity.Book;
import org.example.finalproject_backend.entity.Review;
import org.example.finalproject_backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public ReviewDto reviewEntityToDto(Review review) {
        ReviewDto reviewDto = ReviewDto.builder()
                .reviewId(review.getReviewId())
                .text(review.getText())
                .rating(review.getRating())
                .userFirstName(review.getUser().getFirstName())
                .build();
        return reviewDto;
    }
    public ReviewByUserDto reviewByUserDto(Review review) {
        Book book = review.getBook();

        ReviewByUserDto reviewByUser = ReviewByUserDto.builder()
                .reviewId(review.getReviewId())
                .bookId(book.getBookId())
                .bookName(review.getBook().getTitle())
                .text(review.getText())
                .rating(review.getRating())
                .build();
        return reviewByUser;
    }
    public ReviewPartialDto reviewPartialDto(Review review) {

        ReviewPartialDto reviewPartialDto = ReviewPartialDto.builder()
                .text(review.getText())
                .rating(review.getRating())
                .build();
        return reviewPartialDto;
    }

    public ReviewForAdminDto reviewForAdminDto(Review review) {
        Book book = review.getBook();
        User user = review.getUser();
        ReviewForAdminDto reviewForAdminDto = ReviewForAdminDto.builder()
                .reviewId(review.getReviewId())
                .bookName(book.getTitle())
                .publishYear(book.getPublishYear())
                .bookIsbn(book.getIsbn())
                .reviewText(review.getText())
                .rating(review.getRating())
                .userFirstName(user.getFirstName())
                .userLastName(user.getLastName())
                .userEmail(user.getEmail())
                .build();
        return reviewForAdminDto;
    }
}
