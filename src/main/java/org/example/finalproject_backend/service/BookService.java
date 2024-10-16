package org.example.finalproject_backend.service;

import lombok.RequiredArgsConstructor;
import org.example.finalproject_backend.dto.BookDto;
import org.example.finalproject_backend.entity.Book;
import org.example.finalproject_backend.exception.BookAlreadyExistsException;
import org.example.finalproject_backend.repository.BookRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    //    @Secured("ROLE_ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public Book saveBookAsAdmin(BookDto bookDto) {
        Optional<Book> existingBook = bookRepository.findByIsbn(bookDto.getIsbn());
        if (existingBook.isPresent()) {
            throw new BookAlreadyExistsException(String.format("A book with ISBN %s already exists", bookDto.getIsbn()));
        }
        Book book = Book.builder()
                .title(bookDto.getTitle())
                .author(bookDto.getAuthor())
                .publishYear(bookDto.getPublishYear())
                .isbn(bookDto.getIsbn())
                .numberOfReviews(0)
                .overallRating(0.0)
                .build();
        return bookRepository.save(book);
    }

    public Book saveBookStandard(BookDto bookDto) {
        Optional<Book> existingBook = bookRepository.findByIsbn(bookDto.getIsbn());
        if (existingBook.isPresent()) {
            throw new BookAlreadyExistsException(String.format("A book with ISBN %s already exists", bookDto.getIsbn()));
        }
        Book book = Book.builder()
                .title(bookDto.getTitle())
                .author(bookDto.getAuthor())
                .publishYear(bookDto.getPublishYear())
                .isbn(bookDto.getIsbn())
                .numberOfReviews(0)
                .overallRating(0.0)
                .build();
        return bookRepository.save(book);
    }

    public List<Book> getBookList() {
        return bookRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()->new RuntimeException("book not found"));
        bookRepository.delete(book);
    }

}
