package org.example.finalproject_backend.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Builder
@Getter
@Setter
public class ReviewDto {
    private Long reviewId;
    private String text;
    private int rating;
    private String userFirstName;

//    private String bookIsbn;
//    private String userEmail;
}