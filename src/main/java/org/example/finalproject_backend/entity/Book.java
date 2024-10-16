package org.example.finalproject_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Year;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long bookId;
    private String title;
    private String author;
    private Year publishYear;
    private String isbn;
    private Integer numberOfReviews;
    private Double overallRating;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

//    public void increaseNumberOfReviews() {
//        this.numberOfReviews++;
//    }
//    public void decreaseNumberOfReviews() {
//        this.numberOfReviews--;
//    }

    public void adjustNumberOfReviews() {
        this.numberOfReviews=reviews.size();
    }
}

