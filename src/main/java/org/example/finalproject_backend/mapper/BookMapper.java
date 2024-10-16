package org.example.finalproject_backend.mapper;

import org.example.finalproject_backend.dto.BookDto;
import org.example.finalproject_backend.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    public BookDto bookEntityToDto(Book book) {
     BookDto bookDto = BookDto.builder()
             .bookId(book.getBookId())
             .title(book.getTitle())
             .author(book.getAuthor())
             .publishYear(book.getPublishYear())
             .isbn(book.getIsbn())
             .numberOfReviews(book.getNumberOfReviews())
             .build();

        if (book.getOverallRating() == 0.0) {
            bookDto.setOverallRating("No reviews yet");
        } else {
//            Double percentageRating = book.getOverallRating()*10;
//            bookDto.setOverallRating(String.format("%.0f %%",percentageRating));
            bookDto.setOverallRating(String.format("%.1f/10",book.getOverallRating()));
        }
        return bookDto;
    }

}
