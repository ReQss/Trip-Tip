package com.example.triptip.service.interfaces;

import com.example.triptip.model.reviews.Review;

import java.util.List;
import java.util.Optional;

public interface IReviewService {

    Review createReview(String author, String destination, String title);

    Optional<Review> readReview(String author, String destination, String title);

    List<Review> readAllReviews();
    List<Review> readAllReviewsByDestination(String destinationName);

    boolean updateTitle(String author, String destination, String title, String newTitle);

    boolean updateDescription(String author, String destination, String title, String description);

    boolean deleteReview(String author, String destination, String title);
}
