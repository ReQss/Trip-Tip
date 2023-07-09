package com.example.triptip.service.sorting;

import com.example.triptip.model.destination.Destination;

import java.util.Comparator;

public class DestinationCheapestComparator implements Comparator<Destination> {
    @Override
    public int compare(Destination o1, Destination o2) {
        return o1.getPrice() - o2.getPrice();
    }
}
