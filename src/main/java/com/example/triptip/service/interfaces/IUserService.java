package com.example.triptip.service.interfaces;

import com.example.triptip.model.Tag;
import com.example.triptip.model.user.User;

import java.util.List;

public interface IUserService {
    User createUser(String username, String passwordHash);

    User readUser(String username);

    List<User> readAllUsers();

    boolean updateUsername(String currentUsername, String newUsername);

    boolean updatePasswordHash(String username, String passwordHash);

    boolean addUserPreference(String username, Tag preferenceTag, float value);
    boolean addUserFavourite(String username, String destinationName);
    boolean deleteUserPreference(String username, Tag preferenceTag);

    boolean deleteUserFavourite(String username, String destinationName);

    boolean deleteUser(String username);
}
