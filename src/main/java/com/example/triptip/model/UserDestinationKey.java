package com.example.triptip.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserDestinationKey implements Serializable {

    @Column(name = "user_id")
    Long userId;

    @Column(name = "destination_id")
    Long destinationId;

    public UserDestinationKey(){}

    public UserDestinationKey(Long userId, Long destinationId) {
        this.userId = userId;
        this.destinationId = destinationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDestinationKey that = (UserDestinationKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(destinationId, that.destinationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, destinationId);
    }
}
