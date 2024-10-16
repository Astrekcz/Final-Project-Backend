package org.example.finalproject_backend;

import org.example.finalproject_backend.dto.BookDto;
import org.example.finalproject_backend.entity.Book;
import org.example.finalproject_backend.exception.BookAlreadyExistsException;
import org.example.finalproject_backend.repository.BookRepository;
import org.example.finalproject_backend.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Year;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;


    @Test
    void testSaveBookAsAdminWhenBookAlreadyExists() {
        // Given
        Book book = Book.builder()
                .title("Book Title")
                .author("Author Title")
                .isbn("5435165")
                .build();

        BookDto bookDto = BookDto.builder()
                .title("Title")
                .isbn("5435165")
                .author("Author")
                .bookId((long) 1)
                .publishYear(Year.of(1980))

                .build();

        when(bookRepository.findByIsbn(bookDto.getIsbn())).thenReturn(Optional.of(book));

        // When & Then
        BookAlreadyExistsException exception = assertThrows(
                BookAlreadyExistsException.class,
                () -> bookService.saveBookAsAdmin(bookDto)
        );

        assertEquals("A book with ISBN 5435165 already exists", exception.getMessage());
        verify(bookRepository, never()).save(any(Book.class)); // Ensure save is not called
    }

    @Test
    void testSaveBookAsAdminWhenBookDoesNotExist() {
        // Given
        BookDto bookDto = BookDto.builder()
                .title("Title1")
                .isbn("5435164")
                .author("Author1")
                .bookId((long) 2)
                .publishYear(Year.of(1981))

                .build();
        when(bookRepository.findByIsbn(bookDto.getIsbn())).thenReturn(Optional.empty());
        Book expectedBook = Book.builder()
                .title(bookDto.getTitle())
                .author(bookDto.getAuthor())
                .publishYear(bookDto.getPublishYear())
                .isbn(bookDto.getIsbn())
                .numberOfReviews(0)
                .overallRating(0.0)
                .build();

        when(bookRepository.save(any(Book.class))).thenReturn(expectedBook);

        // When
        Book savedBook = bookService.saveBookAsAdmin(bookDto);

        // Then
        assertNotNull(savedBook);
        assertEquals(bookDto.getIsbn(), savedBook.getIsbn());
        assertEquals(bookDto.getTitle(), savedBook.getTitle());
        verify(bookRepository).save(any(Book.class)); // Ensure save is called
    }




}
