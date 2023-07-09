package com.example.triptip.service.sorting;

import com.example.triptip.model.destination.Destination;

import java.util.Comparator;

public class DestinationExpensiveComparator implements Comparator<Destination> {
    @Override
    public int compare(Destination o1, Destination o2) {
        return o2.getPrice() - o1.getPrice();
    }
}
