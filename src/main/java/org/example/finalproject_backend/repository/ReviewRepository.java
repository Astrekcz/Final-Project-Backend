package org.example.finalproject_backend.repository;

import org.example.finalproject_backend.entity.Book;
import org.example.finalproject_backend.entity.Review;
import org.example.finalproject_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findAllByUserUserId(Long userId);

    Optional<Review> findByBookAndUser(Book book, User user);
}
