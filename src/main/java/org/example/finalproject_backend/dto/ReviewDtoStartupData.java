package org.example.finalproject_backend.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDtoStartupData {
    private String text;
    private int rating;
    private String bookIsbn;
    private String userEmail;
}