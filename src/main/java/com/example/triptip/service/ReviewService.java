package com.example.triptip.service;

import com.example.triptip.model.destination.Destination;
import com.example.triptip.model.destination.DestinationRepository;
import com.example.triptip.model.reviews.Review;
import com.example.triptip.model.reviews.ReviewRepository;
import com.example.triptip.model.user.User;
import com.example.triptip.model.user.UserRepository;
import com.example.triptip.service.interfaces.IReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService implements IReviewService {

    final ReviewRepository reviewRepository;
    final DestinationRepository destinationRepository;
    final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, DestinationRepository destinationRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.destinationRepository = destinationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Review createReview(String author, String destination, String title) {
        if(reviewRepository.existsByAuthor_UsernameAndDestination_NameAndTitle(author,destination,title)) return null;

        Review review = new Review();

        Optional<User> user = userRepository.findByUsername(author);
        if(user.isEmpty()) return null;
        review.setAuthor(user.get());

        Optional<Destination> dest = destinationRepository.findByName(destination);
        if(dest.isEmpty()) return null;
        review.setDestination(dest.get());

        review.setTitle(title);

        return reviewRepository.saveAndFlush(review);
    }

    @Override
    public Optional<Review> readReview(String author, String destination, String title) {
        return reviewRepository.findByAuthor_UsernameAndDestination_NameAndTitle(author,destination,title);
    }

    @Override
    public List<Review> readAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public List<Review> readAllReviewsByDestination(String destinationName){
        return reviewRepository.findAllByDestination_Name(destinationName);
    }

    @Override
    public boolean updateTitle(String author, String destination, String title, String newTitle) {
        Optional<Review> review = reviewRepository.findByAuthor_UsernameAndDestination_NameAndTitle(author,destination,title);
        if(review.isEmpty()) return false;

        review.get().setTitle(title);
        reviewRepository.saveAndFlush(review.get());
        return true;
    }

    @Override
    public boolean updateDescription(String author, String destination, String title, String description) {
        Optional<Review> review = reviewRepository.findByAuthor_UsernameAndDestination_NameAndTitle(author,destination,title);
        if(review.isEmpty()) return false;

        review.get().setDescription(description);
        reviewRepository.saveAndFlush(review.get());
        return true;
    }

    @Override
    public boolean deleteReview(String author, String destination, String title) {
        Optional<Review> review = reviewRepository.findByAuthor_UsernameAndDestination_NameAndTitle(author,destination,title);
        if(review.isEmpty()) return false;

        reviewRepository.deleteById(review.get().getId());
        return true;
    }
}
