package com.example.triptip.unit.service;

import com.example.triptip.model.Tag;
import com.example.triptip.model.destination.Destination;
import com.example.triptip.model.destination.DestinationRepository;
import com.example.triptip.model.user.User;
import com.example.triptip.model.user.UserRepository;
import com.example.triptip.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // No documentation on why lenient is deprecated was found. Don't know what else to use, so I'm sticking with it.
    // Possibly will change it later, if there is time for it.
    @Mock(lenient = true)
    UserRepository userRepository;
    @Mock(lenient = true)
    DestinationRepository destinationRepository;

    @InjectMocks
    UserService service;

    long id1 = 11L;
    String username1 = "JennyO";
    String passwordHash1 = "pass";
    User user1;

    long id2 = 12L;
    String username2 = "JohnO";
    String passwordHash2 = "pass2";
    User user2;

    List<User> allUsers = new ArrayList<>();

    Destination destination1;
    Destination destination2;

    Set<Destination> allDestinations;

    @BeforeEach
    void setUp() {
        //destination1 = new Destination(111L, "I", "i", EnumSet.of(Tag.MOUNTAINS, Tag.HIKING));
        //destination2 = new Destination(112L, "II", "ii", EnumSet.of(Tag.BEACHES, Tag.HIKING));

        allDestinations = new HashSet<>();
        allDestinations.add(destination1);
        allDestinations.add(destination2);

        //user1 = new User(id1, username1, passwordHash1, null, null);
        user1.addPreference(Tag.MUSEUMS, 0.4f);

        //user2 = new User(id2, username2, passwordHash2, null, allDestinations);
        user2.addPreference(Tag.BEACHES, 0.5f);

        allUsers.add(user1);
        allUsers.add(user2);

        when(userRepository.findByUsername(username1)).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername(username2)).thenReturn(Optional.of(user2));

        when(userRepository.existsByUsername(username1)).thenReturn(true);
        when(userRepository.existsByUsername(username2)).thenReturn(true);

        when(userRepository.findAll()).thenReturn(allUsers);

        when(destinationRepository.findByName(destination1.getName())).thenReturn(Optional.of(destination1));
        when(destinationRepository.findByName(destination2.getName())).thenReturn(Optional.of(destination2));

        when(destinationRepository.existsByName(destination1.getName())).thenReturn(true);
        when(destinationRepository.existsByName(destination2.getName())).thenReturn(true);

        when(destinationRepository.findAll()).thenReturn(List.of(destination1, destination2));
    }

    @Test
    void givenUsernameAndPassword_whenCreateUser_thenReturnCorrectUser() {
        String newName = "newUser";
        String newPassword = "newPass";

        //when(userRepository.saveAndFlush(Mockito.any())).thenReturn(new User(1L, newName, newPassword, null, null));
        assertThat(service.createUser(newName, newPassword).getUsername(), equalTo(newName));
    }

    @Test
    void whenReadAllUsers_returnAllUsers() {
        assertThat(service.readAllUsers(), containsInAnyOrder(user1, user2));
    }

    @Test
    void givenUsernameAndTagAndValue_whenAddUserPreference_thenReturnTrue() {
        assertThat(service.addUserPreference(username1, Tag.HIKING, 0.4f), equalTo(true));
    }

    @Test
    void givenNonExistentUsername_whenAddUserPreference_thenReturnFalse() {
        assertThat(service.addUserPreference("nonExistent", Tag.HIKING, 0.4f), equalTo(false));
    }

    @Test
    void givenCorrectUsername_whenReadUser_thenReturnCorrectUser() {
        assertThat(service.readUser(username1), equalTo(user1));
    }

    @Test
    void givenNonExistentNewUsername_whenUpdateUsername_thenReturnTrue() {
        assertThat(service.updateUsername(username1, "newUsername"), equalTo(true));
    }

    @Test
    void givenExistentNewUsername_whenUpdateUsername_thenReturnFalse() {
        assertThat(service.updateUsername(username1, username2), equalTo(false));
    }

    @Test
    void givenNonExistentCurrentUsername_whenUpdateUsername_thenReturnFalse() {
        assertThat(service.updateUsername("currentUsername", "newUsername"), equalTo(false));
    }

    @Test
    void givenCurrentUsernameAndNewPassword_whenUpdatePassword_thenReturnTrue() {
        assertThat(service.updatePasswordHash(username1, "newPass"), equalTo(true));
    }

    @Test
    void givenNonExistentUsername_whenUpdatePassword_thenReturnFalse() {
        assertThat(service.updatePasswordHash("nonExistent", "newPassword"), equalTo(false));
    }

    @Test
    void givenUsernameAndTag_whenDeleteUserPreference_thenReturnTrue() {
        assertThat(service.deleteUserPreference(username1, Tag.MOUNTAINS), equalTo(true));
    }

    @Test
    void givenNonExistentUsername_whenDeleteUserPreference_thenReturnFalse() {
        assertThat(service.deleteUserPreference("nonExistent", Tag.MOUNTAINS), equalTo(false));
    }

    @Test
    void givenUsername_whenDeleteUser_thenReturnTrue() {
        assertThat(service.deleteUser(username1), equalTo(true));
    }

    @Test
    void givenNonExistentUsername_whenDeleteUser_thenReturnFalse() {
        assertThat(service.deleteUser("nonExistent"), equalTo(false));
    }

    @Test
    void givenNewDestination_whenAddUserFavourite_thenReturnTrue() {
        assertThat(service.addUserFavourite(username1, destination1.getName()), equalTo(true));
    }

    @Test
    void givenWrongDestination_whenAddUserFavourite_thenReturnFalse() {
        assertThat(service.addUserFavourite(username2, "wrong"), equalTo(false));
    }

    @Test
    void givenWrongUser_whenAddUserFavourite_thenReturnFalse() {
        assertThat(service.addUserFavourite("wrong", destination2.getName()), equalTo(false));
    }

    @Test
    void givenDestinationName_whenDeleteUserFavourite_thenReturnTrue() {
        assertThat(service.deleteUserFavourite(username2, destination2.getName()), equalTo(true));
    }

    @Test
    void givenWrongDestination_whenDeleteUserFavourite_thenReturnFalse() {
        assertThat(service.deleteUserFavourite(username2, "wrong"), equalTo(false));
    }

    @Test
    void givenWrongUser_whenDeleteUserFavourite_thenReturnFalse() {
        assertThat(service.deleteUserFavourite("wrong", destination2.getName()), equalTo(false));
    }
}
