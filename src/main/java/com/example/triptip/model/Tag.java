package com.example.triptip.model;

public enum Tag {
    HIKING("hiking"),
    BEACHES("beaches"),
    MUSEUMS("museums"),
    MOUNTAINS("mountains");

    private final String name;

    Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
