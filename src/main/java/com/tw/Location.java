package com.tw;

public class Location {

    private String type;
    private Player owner;

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
}
