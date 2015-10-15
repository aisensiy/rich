package com.tw;

import com.tw.exception.RichGameException;

public abstract class Location {

    private Player owner;

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public boolean isLand() {
        return this.getClass() == Land.class;
    }

    public abstract String getName();

    public abstract void process(Player player) throws RichGameException;

    public abstract String getSymbol();
}
