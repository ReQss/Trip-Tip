package com.example.triptip.service;

import com.example.triptip.model.SortOrder;
import com.example.triptip.model.Tag;
import com.example.triptip.model.destination.Destination;
import com.example.triptip.model.destination.DestinationRepository;
import com.example.triptip.service.interfaces.IDestinationService;
import com.example.triptip.service.sorting.DestinationAComparator;
import com.example.triptip.service.sorting.DestinationCheapestComparator;
import com.example.triptip.service.sorting.DestinationExpensiveComparator;
import com.example.triptip.service.sorting.DestinationZComparator;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DestinationService implements IDestinationService {

    final DestinationRepository repository;

    public DestinationService(DestinationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Destination createDestination(String name, String description, int price) {
        if(repository.existsByName(name)) return null;

        Destination destination = new Destination();
        destination.setName(name);
        destination.setDescription(description);
        destination.setPrice(price);

        return repository.saveAndFlush(destination); // Flushes as soon as it saves, because I don't want to deal with it later.
    }

    @Override
    public Destination readDestination(String name) {
        return repository.findByName(name).orElse(null);
    }

    @Override
    public List<Destination> readAllDestinations() {
        return repository.findAll();
    }

    @Override
    public List<Destination> readAllDestinationsSortedWith(SortOrder order) {
        List<Destination> result = readAllDestinations();

        switch (order){
            case CHEAP -> result.sort(new DestinationCheapestComparator());
            case EXPENSIVE -> result.sort(new DestinationExpensiveComparator());
            case A -> result.sort(new DestinationAComparator());
            case Z -> result.sort(new DestinationZComparator());
        }

        return result;
    }

    @Override
    public List<Destination> readAllDestinationsContainingWordAndSortedWith(String word, SortOrder order) {
        List<Destination> full = readAllDestinationsSortedWith(order);
        List<Destination> result = new ArrayList<>();

        if(word == null) return  full;
        if(word.isEmpty()) return full;

        for (Destination destination : full) {
            if (destination.getName().toLowerCase().contains(word.toLowerCase())) {
                result.add(destination);
            }
        }

        return result;
    }

    @Override
    public boolean updateName(String currentName, String newName) {
        if(repository.existsByName(newName)) return false;

        Optional<Destination> destination = repository.findByName(currentName);
        if(destination.isEmpty()) return false;

        destination.get().setName(newName);
        repository.saveAndFlush(destination.get());
        return true;
    }

    @Override
    public boolean updateDescription(String name, String description) {
        Optional<Destination> destination = repository.findByName(name);
        if(destination.isEmpty()) return false;

        destination.get().setDescription(description);
        repository.saveAndFlush(destination.get());
        return true;
    }

    public boolean updatePrice(String name, int price){
        Optional<Destination> destination = repository.findByName(name);
        if(destination.isEmpty()) return false;

        destination.get().setPrice(price);
        repository.saveAndFlush(destination.get());
        return true;
    }

    @Override
    public boolean addPicture(String name, String pictureUrl) {
        Optional<Destination> destination = repository.findByName(name);
        if(destination.isEmpty()) return false;

        if(destination.get().addPictureUrl(pictureUrl)){
            repository.saveAndFlush(destination.get());
            return true;
        }

        return false;
    }

    @Override
    public boolean addDestinationTag(String name, Tag tag) {
        Optional<Destination> destination = repository.findByName(name);
        if(destination.isEmpty()) return false;

        Destination dest = destination.get();
        if(dest.addTag(tag)){
            repository.saveAndFlush(dest);
            return true;
        }

        return false;
    }

    @Override
    public boolean deletePicture(String name, String pictureUrl) {
        Optional<Destination> destination = repository.findByName(name);
        if(destination.isEmpty()) return false;

        if(destination.get().removePictureUrl(pictureUrl)){
            repository.saveAndFlush(destination.get());
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteDestinationTag(String name, Tag tag) {
        Optional<Destination> destination = repository.findByName(name);
        if(destination.isEmpty()) return false;

        Destination dest = destination.get();
        if(dest.removeTag(tag)){
            repository.saveAndFlush(dest);
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteDestination(String name) {
        Optional<Destination> destination = repository.findByName(name);
        if(destination.isEmpty()) return false;

        repository.deleteById(destination.get().getId());
        return true;
    }
}
