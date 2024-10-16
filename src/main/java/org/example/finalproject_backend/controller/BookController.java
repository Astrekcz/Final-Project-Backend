package org.example.finalproject_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.finalproject_backend.dto.BookDto;
import org.example.finalproject_backend.entity.Book;
import org.example.finalproject_backend.mapper.BookMapper;
import org.example.finalproject_backend.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/book")
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/getBooksDto")
    public ResponseEntity<List<BookDto>> getBooksDto() {
        List<BookDto> bookDtoList = new ArrayList<>();
        List<Book> bookList = bookService.getBookList();
        for (int i=0;i<bookList.size();i++) {
            bookDtoList.add(bookMapper.bookEntityToDto(bookList.get(i)));
        }
        return new ResponseEntity<>(bookDtoList, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/saveBookAsAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> saveBookAsAdmin(@Valid @RequestBody BookDto bookDto) {
        try {
            bookService.saveBookAsAdmin(bookDto);
            log.info("Book saved successfully");
            return new ResponseEntity<>("Book saved", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error saving book: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteBookAsAdmin")
    public ResponseEntity<String> deleteBookAsAdmin(Long bookId) {
        try {
            bookService.deleteBook(bookId);
            return new ResponseEntity<>("book was deleted", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/saveBookStandard")
    public ResponseEntity<String> saveBookStandard(@Valid @RequestBody BookDto bookDto) {
        try {
            bookService.saveBookStandard(bookDto);
            log.info("Book saved successfully");
            return new ResponseEntity<>("Book saved", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error saving book: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
