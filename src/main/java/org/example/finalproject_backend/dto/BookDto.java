package org.example.finalproject_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;

@Builder
@Getter
@Setter
public class BookDto {
    private Long bookId;
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Author is required")
    private String author;
    @NotNull(message = "Publish year is required")
    private Year publishYear;
    @NotBlank(message = "ISBN is required")
    private String isbn;
    private Integer numberOfReviews;
    private String overallRating;
}
