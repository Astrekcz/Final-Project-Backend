package org.example.finalproject_backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReviewPartialDto {
    private String text;
    private int rating;
}