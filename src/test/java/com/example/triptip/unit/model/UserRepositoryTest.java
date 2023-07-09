package com.example.triptip.unit.model;

import com.example.triptip.model.Tag;
import com.example.triptip.model.destination.Destination;
import com.example.triptip.model.user.User;
import com.example.triptip.model.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    UserRepository repository;

    String username = "JohnDoe";
    String passwordHash = "password";
    User user;
    Set<Destination> destinations = new HashSet<>();

    @BeforeEach
    void setUp() {
        Map<Tag, Float> preferences1 = new EnumMap<>(Tag.class);
        preferences1.put(Tag.HIKING, 0.5f);
        preferences1.put(Tag.BEACHES, 0.2f);

        Set<Tag> tags1 = EnumSet.of(Tag.MOUNTAINS, Tag.BEACHES);
        Set<Tag> tags2 = EnumSet.of(Tag.HIKING, Tag.MUSEUMS);

        //destinations.add(new Destination(11L, "Jakarta", "Whatever", tags1));
        //destinations.add(new Destination(12L, "Honolulu", "Desc", tags2));

        //user = new User(111L, username, passwordHash, preferences1, destinations);

        entityManager.merge(user);
    }

    @Test
    void givenCorrectUsername_whenFindByUsername_thenReturnCorrectUser() {
        Optional<User> test = repository.findByUsername(username);

        if(test.isEmpty()) Assertions.fail();
        User testUser = test.get();

        assertThat(testUser.getUsername(), equalTo(username));
        assertThat(testUser.getPreferences().keySet(), containsInAnyOrder(Tag.HIKING, Tag.BEACHES));
        assertThat(testUser.getPasswordHash(), equalTo(passwordHash));
    }

    @Test
    void givenCorrectId_whenFindByUsername_thenReturnCorrectUser() {
        Optional<User> test = repository.findByUsername(username);

        if(test.isEmpty()) Assertions.fail();
        User testUser = test.get();

        assertThat(testUser.getUsername(), equalTo(username));
        assertThat(testUser.getPreferences().keySet(), containsInAnyOrder(Tag.HIKING, Tag.BEACHES));
        assertThat(testUser.getPasswordHash(), equalTo(passwordHash));
        assertThat(testUser.getAllFavourites(), hasSize(2));
    }

    @Test
    void whenSetMethods_thenCorrectlySetValues() {

        Set<Tag> tags = new HashSet<>();
        tags.add(Tag.BEACHES);

        user.addPreference(Tag.MUSEUMS, 0.1f);
        user.removePreference(Tag.BEACHES);
        user.setPasswordHash("new");
        user.setUsername("Jason Doe");
        user.addFavouriteDestination(new Destination(13L, "III", "iii", tags));

        repository.saveAndFlush(user);

        Optional<User> test = repository.findByUsername(user.getUsername());

        if(test.isEmpty()) Assertions.fail();
        User testUser = test.get();

        assertThat(testUser.getUsername(), equalTo("Jason Doe"));
        assertThat(testUser.getPreferences().keySet(), containsInAnyOrder(Tag.HIKING, Tag.MUSEUMS));
        assertThat(testUser.getPasswordHash(), equalTo("new"));
        assertThat(testUser.getAllFavourites(), hasSize(3));
    }

    @Test
    void givenNewUser_whenFindAll_returnAllUsers() {
        User newUser = new User();
        newUser.setId(112L);
        newUser.setUsername("notNullName");
        newUser.setPasswordHash("notNullName");
        repository.save(newUser);

        assertThat(repository.findAll(), hasSize(2));
    }
}
