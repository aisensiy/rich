package com.tw;

public class Location {

    private String type;
    private Player owner;
    private int price = 200;

    public Location(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getPrice() {
        return price;
    }
}
