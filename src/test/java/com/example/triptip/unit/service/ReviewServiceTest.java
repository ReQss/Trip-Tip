package com.example.triptip.unit.service;

import com.example.triptip.model.Tag;
import com.example.triptip.model.destination.Destination;
import com.example.triptip.model.destination.DestinationRepository;
import com.example.triptip.model.reviews.Review;
import com.example.triptip.model.reviews.ReviewRepository;
import com.example.triptip.model.user.User;
import com.example.triptip.model.user.UserRepository;
import com.example.triptip.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock(lenient = true)
    ReviewRepository reviewRepository;
    @Mock(lenient = true)
    DestinationRepository destinationRepository;
    @Mock(lenient = true)
    UserRepository userRepository;

    @InjectMocks
    ReviewService service;

    long reviewId = 1111L;
    String reviewTitle = "Title";
    String reviewDescription = "Description";
    Review review;

    long destinationId = 111L;
    String destinationName = "Jakarta";
    String destinationDescription = """
            Jakarta (/dʒəˈkɑːrtə/; Indonesian pronunciation: [dʒaˈkarta] (listen), Betawi: Jakarte, formerly Dutch: Batavia), 
            officially the Special Capital Region of Jakarta (Indonesian: Daerah Khusus Ibukota Jakarta), is the capital and 
            largest city of Indonesia. Lying on the north-west coast of Java, the world's most populous island, Jakarta is the 
            largest city in Southeast Asia, and serves as the diplomatic capital of ASEAN.\s""";

    Set<Tag> destinationTags = EnumSet.of(Tag.MUSEUMS, Tag.HIKING);
    Destination destination;

    long userId = 11L;
    String username = "username";
    String passwordHash = "password";
    EnumMap<Tag, Float> preferences = new EnumMap<>(Tag.class);
    User user;

    @BeforeEach
    void setUp() {
        preferences.put(Tag.BEACHES, 0.4f);
        preferences.put(Tag.HIKING, 0.4f);
        //user = new User(userId, username, passwordHash, preferences, null);

        destinationTags.add(Tag.MOUNTAINS);
        destinationTags.add(Tag.BEACHES);
        //destination = new Destination(destinationId, destinationName, destinationDescription, destinationTags);

        review = new Review(reviewId, user, destination, reviewTitle, reviewDescription);

        when(reviewRepository.findByAuthor_UsernameAndDestination_NameAndTitle(username, destinationName, reviewTitle))
                .thenReturn(Optional.of(review));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(destinationRepository.findByName(destinationName)).thenReturn(Optional.of(destination));
        when(reviewRepository.existsByAuthor_UsernameAndDestination_NameAndTitle(username, destinationName, reviewTitle))
                .thenReturn(true);
        when(userRepository.existsByUsername(username)).thenReturn(true);
        when(destinationRepository.existsByName(destinationName)).thenReturn(true);
    }

    @Test
    void givenCorrectReviewParameters_whenCreateReview_thenReturnReview() {
        Review newReview = new Review(1112L, user, destination, "newTitle", "newDescription");

        when(reviewRepository.saveAndFlush(Mockito.any())).thenReturn(newReview);
        assertThat(service.createReview(newReview.getAuthor().getUsername(),
                        newReview.getDestination().getName(),
                        newReview.getTitle()),
                equalTo(newReview));
    }

    @Test
    void givenAlreadyExistentReview_whenCreateReview_thenReturnNull() {
        Review newReview = new Review(1112L, user, destination, reviewTitle, reviewDescription);

        assertThat(service.createReview(newReview.getAuthor().getUsername(),
                        newReview.getDestination().getName(),
                        newReview.getTitle()),
                equalTo(null));
    }

    @Test
    void givenCorrectParameters_whenReadReview_thenReturnOptionalOfReview() {
        assertThat(service.readReview(username, destinationName, reviewTitle), equalTo(Optional.of(review)));
    }

    @Test
    void givenWrongParameters_whenReadReview_thenReturnEmptyOptional() {
        assertThat(
                service.readReview("username", "destinationName", "reviewTitle"), 
                equalTo(Optional.empty()));
    }

    @Test
    void whenReadAllReviews_thenReturnListOfReviews() {
        Review newReview = new Review(1112L, user, destination, "newTitle", "newDescription");
        when(reviewRepository.findAll()).thenReturn(List.of(review, newReview));
        assertThat(service.readAllReviews(), containsInAnyOrder(review, newReview));
    }

    @Test
    void givenCorrectDestinationName_whenReadAllByDestination_thenReturnListOfReviews() {
        Review newReview = new Review(1112L, user, destination, "newTitle", "newDescription");
        when(reviewRepository.findAllByDestination_Name(destinationName)).thenReturn(List.of(review, newReview));
        assertThat(service.readAllReviewsByDestination(destinationName), containsInAnyOrder(review, newReview));
    }

    @Test
    void givenCorrectParameters_whenUpdateTitle_thenReturnTrue() {
        assertThat(service.updateTitle(username, destinationName, reviewTitle, "newTitle"), equalTo(true));
    }

    @Test
    void givenWrongParameters_whenUpdateTitle_thenReturnFalse() {
        assertThat(service.updateTitle("wrong", "wrong", "wrong", "wrong"), equalTo(false));
    }

    @Test
    void givenCorrectParameters_whenUpdateDescription_thenReturnTrue() {
        assertThat(service.updateDescription(username, destinationName, reviewTitle, "newTitle"), equalTo(true));
    }

    @Test
    void givenWrongParameters_whenUpdateDescription_thenReturnFalse() {
        assertThat(service.updateDescription("wrong", "wrong", "wrong", "wrong"), equalTo(false));
    }

    @Test
    void givenCorrectParameters_whenDeleteReview_thenReturnTrue() {
        assertThat(service.deleteReview(username, destinationName, reviewTitle), equalTo(true));
    }

    @Test
    void givenWrongParameters_whenDeleteReview_thenReturnFalse() {
        assertThat(service.deleteReview("wrong", "wrong", "wrong"), equalTo(false));
    }
}