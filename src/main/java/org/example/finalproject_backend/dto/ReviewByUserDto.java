package org.example.finalproject_backend.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

@Builder
@Getter
@Setter
public class ReviewByUserDto {
    private Long reviewId;
    private Long bookId;
    private String bookName;
    private String text;
    private int rating;
}