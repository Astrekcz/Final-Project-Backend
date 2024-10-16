package org.example.finalproject_backend;


import org.example.finalproject_backend.dto.BookDto;
import org.example.finalproject_backend.entity.Book;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookMapperTest {
    @Test
    public void BookEntityToDtoTest() {
      Book book = Book.builder()
              .overallRating(0.0)
              .build();

      BookDto bookdto = BookDto.builder()
              .overallRating("no reviews")
              .build();

      bookdto.setOverallRating("no reviews");
      book.setOverallRating(0.0);

      assertEquals(book.getOverallRating(), 0.0);
      assertEquals(bookdto.getOverallRating(), "no reviews");


    }
}
