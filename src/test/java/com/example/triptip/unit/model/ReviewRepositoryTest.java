package com.example.triptip.unit.model;

import com.example.triptip.model.Tag;
import com.example.triptip.model.destination.Destination;
import com.example.triptip.model.destination.DestinationRepository;
import com.example.triptip.model.reviews.Review;
import com.example.triptip.model.reviews.ReviewRepository;
import com.example.triptip.model.user.User;
import com.example.triptip.model.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DestinationRepository destinationRepository;

    User user;
    Destination destination;
    String title = "A beautiful trip to Jakarta";
    String description = """
            dignissim enim sit amet venenatis urna cursus eget nunc scelerisque viverra mauris in aliquam sem fringilla 
            ut morbi tincidunt augue interdum velit euismod in pellentesque massa placerat duis ultricies lacus sed turpis 
            tincidunt id aliquet risus feugiat in ante metus dictum at tempor commodo ullamcorper a lacus vestibulum sed 
            arcu non odio euismod lacinia at quis risus sed vulputate odio ut enim blandit volutpat maecenas volutpat blandit 
            aliquam etiam erat velit scelerisque in dictum non consectetur a erat nam at lectus urna duis convallis convallis 
            tellus id interdum velit laoreet id donec ultrices tincidunt arcu non sodales neque sodales ut etiam sit amet 
            nisl purus in mollis nunc sed id semper risus in hendrerit gravida rutrum quisque non tellus orci ac auctor 
            augue mauris augue neque gravida in fermentum et sollicitudin ac orci phasellus egestas tellus rutrum tellus 
            pellentesque eu tincidunt tortor aliquam nulla facilisi cras fermentum odio eu feugiat pretium nibh ipsum consequat 
            nisl vel pretium lectus quam id leo in vitae turpis massa sed elementum tempus egestas sed sed risus pretium 
            quam vulputate dignissim suspendisse in est ante in nibh mauris cursus mattis molestie a iaculis at erat pellentesque 
            adipiscing commodo elit at imperdiet dui accumsan sit amet""";

    Review review;

    String destinationName = "Jakarta";
    String username = "John Bobby";

    @BeforeEach
    void setUp() {
        //user = new User(11L, username, "pass", null, null);
        user.addPreference(Tag.BEACHES, 0.9f);
        userRepository.saveAndFlush(user);

        //destination = new Destination(11L,destinationName, "wolololo", null);
        destination.addTag(Tag.BEACHES);
        destinationRepository.saveAndFlush(destination);

        if(userRepository.findByUsername(username).isEmpty() || destinationRepository.findByName(destinationName).isEmpty()) return;
        review = new Review();
        review.setTitle(title);
        review.setDescription(description);
        review.setDestination(destinationRepository.findByName(destinationName).get());
        review.setAuthor(userRepository.findByUsername(username).get());
        reviewRepository.saveAndFlush(review);
    }

    @Test
    void givenCorrectId_whenFindById_thenReturnReview() {
        Optional<Review> test = reviewRepository.findById(review.getId());

        if(test.isEmpty()) Assertions.fail();
        Review testReview = test.get();

        assertThat(testReview.getAuthor().getUsername(), equalTo(user.getUsername()));
        assertThat(testReview.getDestination().getName(), equalTo(destination.getName()));
        assertThat(testReview.getTitle(), equalTo(title));
        assertThat(testReview.getDescription(), equalTo(description));
    }

    @Test
    void whenSetMethodsRun_thenReturnReviewWithNewValues() {

        User newUser = new User();
        newUser.setUsername("notNullName");
        newUser.setPasswordHash("notNullName");
        userRepository.saveAndFlush(newUser);

        if(userRepository.findByUsername("notNullName").isEmpty()) Assertions.fail();
        review.setAuthor(userRepository.findByUsername("notNullName").get());

        Destination newDestination = new Destination();
        newDestination.setName("notNullName");
        destinationRepository.saveAndFlush(newDestination);

        if(destinationRepository.findByName("notNullName").isEmpty()) Assertions.fail();
        review.setDestination(destinationRepository.findByName("notNullName").get());

        String newTitle = "newTitle";
        review.setTitle(newTitle);
        String newDescription = "newDescription";
        review.setDescription(newDescription);

        reviewRepository.saveAndFlush(review);
        Optional<Review> test = reviewRepository.findById(review.getId());

        if(test.isEmpty()) Assertions.fail();
        Review testReview = test.get();

        assertThat(testReview.getAuthor(), equalTo(newUser));
        assertThat(testReview.getDestination(), equalTo(newDestination));
        assertThat(testReview.getTitle(), equalTo(newTitle));
        assertThat(testReview.getDescription(), equalTo(newDescription));
    }

    @Test
    void givenNewReview_whenFindAll_thenReturnAllReviews() {
        User newUser = new User();
        newUser.setId(112L);
        newUser.setUsername("notNullName");
        newUser.setPasswordHash("notNullName");
        userRepository.save(newUser);

        if(userRepository.findByUsername("notNullName").isEmpty()) Assertions.fail();
        review.setAuthor(userRepository.findByUsername("notNullName").get());

        Destination newDestination = new Destination();
        newDestination.setId(12L);
        newDestination.setName("notNullName");
        destinationRepository.save(newDestination);

        if(destinationRepository.findByName("notNullName").isEmpty()) Assertions.fail();
        review.setDestination(destinationRepository.findByName("notNullName").get());

        Review newReview = new Review();
        newReview.setDestination(destinationRepository.findByName("notNullName").get());
        newReview.setAuthor(userRepository.findByUsername("notNullName").get());
        newReview.setTitle("newTitle");
        newReview.setDescription("newDescription");
        reviewRepository.save(newReview);

        assertThat(reviewRepository.findAll(), hasSize(2));
    }

    @Test
    void givenCorrectUserAndDestinationAndTitle_whenFindByUsernameDestinationNameAndTitle_thenReturnReview() {
        Optional<Review> test =
                reviewRepository.findByAuthor_UsernameAndDestination_NameAndTitle(username, destinationName, title);

        if(test.isEmpty()) Assertions.fail();
        Review testReview = test.get();

        assertThat(testReview.getAuthor().getUsername(), equalTo(user.getUsername()));
        assertThat(testReview.getDestination().getName(), equalTo(destination.getName()));
        assertThat(testReview.getTitle(), equalTo(title));
        assertThat(testReview.getDescription(), equalTo(description));
    }

    @Test
    void givenCorrectUserAndDestinationAndTitle_whenExistsByUsernameDestinationNameAndTitle_thenReturnTrue() {
        boolean test =
                reviewRepository.existsByAuthor_UsernameAndDestination_NameAndTitle(username, destinationName, title);

        assertThat(test, equalTo(true));
    }

    @Test
    void givenCorrectDestinationName_whenFindAllByDestination_thenReturnListOfReviews() {
        Review newReview = new Review();
        newReview.setTitle("Title");
        newReview.setDescription("Desc");
        if(userRepository.findByUsername(username).isEmpty() || destinationRepository.findByName(destinationName).isEmpty()) return;
        newReview.setAuthor(userRepository.findByUsername(username).get());
        newReview.setDestination(destinationRepository.findByName(destinationName).get());
        reviewRepository.saveAndFlush(newReview);

        assertThat(reviewRepository.findAllByDestination_Name(destinationName), containsInAnyOrder(newReview, review));
    }
}