package com.example.triptip.service;

import com.example.triptip.model.Tag;
import com.example.triptip.model.destination.Destination;
import com.example.triptip.model.destination.DestinationRepository;
import com.example.triptip.model.user.User;
import com.example.triptip.model.user.UserRepository;
import com.example.triptip.service.interfaces.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    final UserRepository userRepository;
    final DestinationRepository destinationRepository;

    public UserService(UserRepository userRepository, DestinationRepository destinationRepository) {
        this.userRepository = userRepository;
        this.destinationRepository = destinationRepository;
    }

    @Override
    public User createUser(String username, String passwordHash){

        if(userRepository.existsByUsername(username)) return null;

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordHash);

        return userRepository.saveAndFlush(user);
    }

    @Override
    public User readUser(String username){
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElse(null);
    }

    @Override
    public List<User> readAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean updateUsername(String currentUsername, String newUsername){
        if(userRepository.existsByUsername(newUsername)) return false;

        Optional<User> user = userRepository.findByUsername(currentUsername);

        if(user.isEmpty()) return false;
        user.get().setUsername(newUsername);

        userRepository.saveAndFlush(user.get()); // Flushing immediately because I'm lazy and don't want to deal with flushing later.
        return true;
    }

    @Override
    public boolean updatePasswordHash(String username, String passwordHash){
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isEmpty()) return false;
        user.get().setPasswordHash(passwordHash);

        userRepository.saveAndFlush(user.get());
        return true;
    }

    @Override
    public boolean addUserPreference(String username, Tag preferenceTag, float value){
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isEmpty()) return false;
        user.get().addPreference(preferenceTag, value);

        userRepository.saveAndFlush(user.get());
        return true;
    }

    @Override
    @Transactional
    public boolean addUserFavourite(String username, String destinationName) {
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Destination> destination = destinationRepository.findByName(destinationName);

        if(user.isEmpty() || destination.isEmpty()) return false;

        return user.get().addFavouriteDestination(destination.get());
    }

    @Override
    public boolean deleteUserPreference(String username, Tag preferenceTag){
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isEmpty()) return false;
        user.get().removePreference(preferenceTag);

        userRepository.saveAndFlush(user.get());
        return true;
    }

    @Override
    @Transactional
    public boolean deleteUserFavourite(String username, String destinationName) {
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Destination> destination = destinationRepository.findByName(destinationName);

        if(user.isEmpty() || destination.isEmpty()) return false;

        return user.get().removeFavourite(destination.get());
    }

    @Override
    public boolean deleteUser(String username){
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isEmpty()) return false;
        userRepository.deleteById(user.get().getId());

        return true;
    }
}
