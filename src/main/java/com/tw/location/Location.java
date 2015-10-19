package com.tw.location;

import com.tw.Player;
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

    public boolean isEmptyLand() {
        if (!isLand()) {
            return false;
        }
        Land land = (Land) this;
        return land.getOwner() == null;
    }

    public String getMessage() {
        return "";
    };

    public boolean isToolShop() {
        return this.getClass() == ToolShop.class;
    }

}
