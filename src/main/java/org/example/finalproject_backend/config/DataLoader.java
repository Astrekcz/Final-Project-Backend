package org.example.finalproject_backend.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.finalproject_backend.dto.ReviewDtoStartupData;
import org.example.finalproject_backend.entity.Book;
import org.example.finalproject_backend.entity.Review;
import org.example.finalproject_backend.entity.User;
import org.example.finalproject_backend.repository.BookRepository;
import org.example.finalproject_backend.repository.ReviewRepository;
import org.example.finalproject_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        loadBooks();
        loadUsers();
        loadReviews();
    }

    private void loadBooks() throws Exception {
        InputStream inputStream = new ClassPathResource("startup_data/books.json").getInputStream();
        List<Book> books = objectMapper.readValue(inputStream, new TypeReference<List<Book>>() {});

        for (Book book : books) {
            Optional<Book> existingBook = bookRepository.findByIsbn(book.getIsbn());
            if (existingBook.isEmpty()) {
                bookRepository.save(book);
            }
        }
    }

    private void loadUsers() throws Exception {
        InputStream inputStream = new ClassPathResource("startup_data/users.json").getInputStream();
        List<User> users = objectMapper.readValue(inputStream, new TypeReference<List<User>>() {});

        for (User user : users) {
            Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser.isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
            }
        }
    }

    private void loadReviews() throws Exception {
        InputStream inputStream = new ClassPathResource("startup_data/reviews.json").getInputStream();
        List<ReviewDtoStartupData> reviewDtos = objectMapper.readValue(inputStream, new TypeReference<List<ReviewDtoStartupData>>() {});

        for (ReviewDtoStartupData reviewDto : reviewDtos) {
            Optional<Book> book = bookRepository.findByIsbn(reviewDto.getBookIsbn());
            Optional<User> user = userRepository.findByEmail(reviewDto.getUserEmail());

            if (book.isPresent() && user.isPresent()) {
                Optional<Review> existingReviews = reviewRepository.findByBookAndUser(book.get(), user.get());
                if (existingReviews.isEmpty()) {
                    Review review = new Review();
                    review.setText(reviewDto.getText());
                    review.setRating(reviewDto.getRating());
                    review.setBook(book.get());
                    review.setUser(user.get());
                    reviewRepository.save(review);
                }
            }
        }
    }
}
