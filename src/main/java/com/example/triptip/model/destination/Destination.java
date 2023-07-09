package com.example.triptip.model.destination;

import com.example.triptip.model.Tag;
import com.example.triptip.model.UserDestination;
import jakarta.persistence.*;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(length = 4096)
    private String description;

    private Integer price;

    @ElementCollection
    private Set<Tag> tags;

    @ElementCollection
    @CollectionTable(name = "destination_pictures", joinColumns = @JoinColumn(name = "destination_id"))
    @Column(length = 4096)
    private Set<String> pictureUrlSet;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "destination", orphanRemoval = true)
    private Set<UserDestination> usersThatFavourited;

    public Destination(Long id, String name, String description, Set<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tags = tags;
    }

    public Destination() {

    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public boolean addPictureUrl(String url) {
        if (this.pictureUrlSet == null) this.pictureUrlSet = new HashSet<>();
        return this.pictureUrlSet.add(url);
    }

    public boolean removePictureUrl(String url) {
        return this.pictureUrlSet.remove(url);
    }

    public Set<String> getPictureUrlSet() {
        return pictureUrlSet;
    }

    public String getPictureUrl() {
        if(pictureUrlSet.isEmpty()) return "";
        return (String) pictureUrlSet.toArray()[0];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public String getName_equal(String word){
        String [] split = this.description.split(" ");
        for(String s:split){
            if(s.equals(word))return this.name;
        }
        return null;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public boolean addTag(Tag tag) {
        if (this.tags == null) this.tags = EnumSet.noneOf(Tag.class);
        return this.tags.add(tag);
    }

    public boolean removeTag(Tag tag) {
        return this.tags.remove(tag);
    }

    public boolean removeUserFavourite(UserDestination userDestination){
        return usersThatFavourited.remove(userDestination);
    }

    public boolean equal(String name){
        return name.equals("index");
    }
    public boolean containWord(String word){

            String[] tab = this.description.split(" ");
            for (String s : tab) {
                if (s.equals(word)) return true;
            }

        return false;
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
        Destination that = (Destination) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    public String  searchDestinations(String name){
        if(this.name.equals(name))return this.name;
        return null;
    }
}
