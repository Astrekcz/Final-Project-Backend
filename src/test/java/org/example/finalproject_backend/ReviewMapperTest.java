package org.example.finalproject_backend;

import org.example.finalproject_backend.dto.ReviewByUserDto;
import org.example.finalproject_backend.dto.ReviewDto;
import org.example.finalproject_backend.entity.Book;
import org.example.finalproject_backend.entity.Review;
import org.example.finalproject_backend.entity.User;
import org.example.finalproject_backend.mapper.ReviewMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReviewMapperTest {


    private final ReviewMapper reviewMapper = new ReviewMapper();


    @Test
    public void testReviewEntityToDto() {
        Review review = new Review();
        review.setReviewId(1L);
        review.setText("Great book");
        review.setRating(5);
        User user = new User();
        user.setFirstName("Pepa");
        review.setUser(user);
        Book book = new Book();
        book.setTitle("Hladová Srna");
        review.setBook(book);

        ReviewDto reviewDto = reviewMapper.reviewEntityToDto(review);

        assertEquals(review.getReviewId(), reviewDto.getReviewId());
        assertEquals(review.getText(), reviewDto.getText());
        assertEquals(review.getRating(), reviewDto.getRating());
        assertEquals(review.getUser().getFirstName(), reviewDto.getUserFirstName());
        assertEquals(review.getBook().getTitle(), book.getTitle());

    }
}
