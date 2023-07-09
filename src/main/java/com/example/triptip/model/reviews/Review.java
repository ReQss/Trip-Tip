package com.example.triptip.model.reviews;

import com.example.triptip.model.destination.Destination;
import com.example.triptip.model.user.User;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private User author;

    @OneToOne
    private Destination destination;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 4096)
    private String description;

    public Review(Long id, User author, Destination destination, String title, String description) {
        this.id = id;
        this.author = author;
        this.destination = destination;
        this.title = title;
        this.description = description;
    }

    public Review() {
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        Review review = (Review) o;
        return Objects.equals(id, review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
