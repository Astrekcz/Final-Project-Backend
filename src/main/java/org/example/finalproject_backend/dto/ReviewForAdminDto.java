package org.example.finalproject_backend.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;

@Builder
@Getter
@Setter
public class ReviewForAdminDto {
    private Long reviewId;
    private String bookName;
    private Year publishYear;
    private String bookIsbn;
    private String reviewText;
    private int rating;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
}