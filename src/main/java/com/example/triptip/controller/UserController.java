package com.example.triptip.controller;

import com.example.triptip.model.Tag;
import com.example.triptip.model.destination.Destination;
import com.example.triptip.model.user.User;
import com.example.triptip.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
public class UserController {

    private final UserService service;
    private User currentUser; //NEVER DO THAT. THIS IS BAD AND IS ONLY FOR THE SAKE OF PRESENTATION.

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/users/register")
    public ResponseEntity<User> createUser(@ModelAttribute User user) {
        User created = service.createUser(user.getUsername(), user.getPasswordHash());
        currentUser = created;
        return ResponseEntity.ok(created);
    }

    @GetMapping("/users/login")
    public ResponseEntity<User> loginUser(@RequestParam("username") String username) {
        if (service.readUser(username) == null) return ResponseEntity.badRequest().body(null);
        currentUser = service.readUser(username);
        return ResponseEntity.ok(service.readUser(username));
    }

    @GetMapping("/users/getFav")
    public ResponseEntity<Set<Destination>> getFavouritesForCurrentUser() {
        if (currentUser == null) return ResponseEntity.badRequest().body(null);
        currentUser = service.readUser(currentUser.getUsername());
        return ResponseEntity.ok(currentUser.getAllFavourites());
    }

    @GetMapping("/users/getPref")
    public ResponseEntity<Map<Tag, Float>> getUserPreferencesForCurrentUser() {
        if (currentUser == null) return ResponseEntity.badRequest().body(null);
        currentUser = service.readUser(currentUser.getUsername());
        return ResponseEntity.ok(currentUser.getPreferences());
    }

    @PostMapping("/users/addFav")
    public ResponseEntity<Boolean> addFavouriteForCurrentUser(@RequestParam("name") String destinationName) {
        if (currentUser == null) return ResponseEntity.badRequest().body(null);
        currentUser = service.readUser(currentUser.getUsername());
        return ResponseEntity.ok(service.addUserFavourite(currentUser.getUsername(), destinationName));
    }

    @PostMapping("/users/addPref")
    public ResponseEntity<Boolean> addPreferenceForCurrentUser(@RequestParam("pref") String preference,
                                                               @RequestParam("value") float value) {
        if (currentUser == null) return ResponseEntity.badRequest().body(null);
        return ResponseEntity.ok(service.addUserPreference(currentUser.getUsername(), Tag.valueOf(preference), value));
    }
}
