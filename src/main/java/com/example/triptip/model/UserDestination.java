package com.example.triptip.model;

import com.example.triptip.model.destination.Destination;
import com.example.triptip.model.user.User;
import jakarta.persistence.*;

@Entity
public class UserDestination {

    @EmbeddedId
    UserDestinationKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("destinationId")
    @JoinColumn(name = "destination_id")
    private Destination destination;

    public UserDestination() {
    }

    public UserDestination(User user, Destination destination) {
        this.user = user;
        this.destination = destination;
        this.id = new UserDestinationKey(user.getId(), destination.getId());
    }

    public UserDestinationKey getId() {
        return id;
    }

    public void setId(UserDestinationKey id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }
}
