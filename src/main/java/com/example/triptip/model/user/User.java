package com.example.triptip.model.user;

import com.example.triptip.model.Tag;
import com.example.triptip.model.UserDestination;
import com.example.triptip.model.destination.Destination;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 32)
    private String username;

    @Column(nullable = false)
    private String passwordHash;


    @ElementCollection
    @MapKeyColumn(name = "preference_tag")
    @Column(name = "preference_weight")
    @CollectionTable(name = "preferences", joinColumns = @JoinColumn(name = "user_id"))
    private Map<Tag, Float> preferences;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user", orphanRemoval = true)
    private Set<UserDestination> favourites;

    public User(Long id, String username, String passwordHash, Map<Tag, Float> preferences) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.preferences = preferences;
    }

    public User() {

    }

    public Map<Tag, Float> getPreferences() {
        return preferences;
    }

    public void addPreference(Tag key, float value) {
        if (this.preferences == null) this.preferences = new EnumMap<>(Tag.class);
        this.preferences.put(key, value);
    }

    public Float removePreference(Tag key) {
        if (this.preferences == null) return null;
        return this.preferences.remove(key);
    }

    public Set<Destination> getAllFavourites() {
        Set<Destination> destinations = new HashSet<>();
        for (UserDestination userDestination : favourites) {
            destinations.add(userDestination.getDestination());
        }
        return destinations;
    }

    public boolean hasFavourite(String name) {
        if (favourites == null) return false;
        for (UserDestination userDestination : favourites) {
            if (Objects.equals(userDestination.getDestination().getName(), name)) return true;
        }
        return false;
    }

    public boolean removeFavourite(Destination destinationToRemove) {
        if (favourites == null) return false;
        UserDestination userDestination = new UserDestination();
        for (UserDestination nextUserDestination : favourites) {
            if(nextUserDestination.getDestination() == destinationToRemove) userDestination = nextUserDestination;
        }
        return favourites.remove(userDestination) && destinationToRemove.removeUserFavourite(userDestination);
    }

    public boolean addFavouriteDestination(Destination destinationToAdd) {
        if (favourites == null) favourites = new HashSet<>();
        return favourites.add(new UserDestination(this, destinationToAdd));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
