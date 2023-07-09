package com.example.triptip.model;

public enum SortOrder {
    CHEAP(0, "Price ($ to $$$)"),
    EXPENSIVE(1, "Price ($$$ to $)"),
    A(2, "Name (A to Z)"),
    Z(3, "Name (Z to A)");

    private final int id;
    private String displayName;

    SortOrder(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static SortOrder getById(int id){
        for(SortOrder order : values()){
            if(order.id == id) return order;
        }
        return CHEAP;
    }
}
