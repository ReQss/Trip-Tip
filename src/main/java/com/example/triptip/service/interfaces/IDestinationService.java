package com.example.triptip.service.interfaces;

import com.example.triptip.model.SortOrder;
import com.example.triptip.model.Tag;
import com.example.triptip.model.destination.Destination;

import java.util.List;

public interface IDestinationService {
    Destination createDestination(String name, String description, int price);

    Destination readDestination(String name);

    List<Destination> readAllDestinations();

    List<Destination> readAllDestinationsSortedWith(SortOrder order);
    List<Destination> readAllDestinationsContainingWordAndSortedWith(String word, SortOrder order);

    boolean updateName(String currentName, String newName);

    boolean updateDescription(String name, String description);
    boolean addPicture(String name, String pictureUrl);

    boolean addDestinationTag(String name, Tag tag);

    boolean deletePicture(String name, String pictureUrl);

    boolean deleteDestinationTag(String name, Tag tag);

    boolean deleteDestination(String name);
}
