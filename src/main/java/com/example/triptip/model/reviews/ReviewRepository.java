package com.example.triptip.model.reviews;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByAuthor_UsernameAndDestination_NameAndTitle(String authorName, String destinationName, String title);
    Optional<Review> findByAuthor_UsernameAndDestination_NameAndTitle(String authorName, String destinationName, String title);
    List<Review> findAllByDestination_Name(String destination);
}
