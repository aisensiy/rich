package com.tw;

import com.tw.exception.CannotUpgradeLandException;
import com.tw.exception.NoEnoughFoundException;
import com.tw.exception.RichGameException;
import com.tw.exception.TypeCastException;

public class Player {
    private final String name;
    private static final String[] playerNames = new String[] {
        "钱夫人",
        "阿土伯",
        "孙小美",
        "金贝贝"
    };
    private int currrentLocationIndex = 0;
    private Game game;
    private int funding;

    private Player(Game game, String name, int funding) {
        this.game = game;
        this.name = name;
        this.funding = funding;
    }

    public static Player createPlayer(Game game, int index) {
        Player player = new Player(game, playerNames[index], Game.DEFAULT_FUNDING);
        return player;
    }

    public static Player createPlayer(Game game, int index, int funding) {
        Player player = new Player(game, playerNames[index], funding);
        return player;
    }

    public String getName() {
        return name;
    }

    public int getLocationIndex() {
        return currrentLocationIndex;
    }

    public void setCurrentLocation(int currentLocation) {
        this.currrentLocationIndex = currentLocation;
    }

    public void go(int step) {
        currrentLocationIndex = (currrentLocationIndex + step) % Game.MAP_SIZE;
    }

    public void resetLocation() {
        currrentLocationIndex = 0;
    }

    public Location getCurrentLocation() {
        return game.location(currrentLocationIndex);
    }

    public void buyEmptyLand() throws RichGameException {
        Land land = castToLand(game.location(currrentLocationIndex));

        if (land.getOwner() != null) {
            throw new CannotBuyLocationException("current location already get an owner");
        }

        ensureFoundingIsEnough(land.getPrice());
        getCurrentLocation().setOwner(this);
        funding -= land.getPrice();
    }

    private void ensureFoundingIsEnough(int price) throws NoEnoughFoundException {
        if (funding < price) {
            throw new NoEnoughFoundException("");
        }
    }

    public int getFunding() {
        return funding;
    }

    public void upgradeLand() throws RichGameException {
        Land land = castToLand(game.location(currrentLocationIndex));

        if (land.getOwner() != this) {
            throw new CannotUpgradeLandException("can not upgrade land which is not belong to you");
        }

        if (land.isHighestLevel()) {
            throw new CannotUpgradeLandException("can not upgrade land with highest level");
        }

        ensureFoundingIsEnough(land.getPrice());

        funding -= land.getPrice();
        land.upgradeLevel();
    }

    private Land castToLand(Location location) throws TypeCastException {
        if (!location.isLand()) {
            throw new TypeCastException("can not convert other type to land");
        }
        return (Land) location;
    }
}
