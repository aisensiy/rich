package com.tw;

import com.tw.exception.NoEnoughFoundException;
import com.tw.exception.RichGameException;

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
        Location location = game.location(currrentLocationIndex);
        if (location.getType() != "land") {
            throw new CannotBuyLocationException("current location is not a land");
        }
        if (location.getOwner() != null) {
            throw new CannotBuyLocationException("current location already get an owner");
        }

        if (funding < location.getPrice()) {
            throw new NoEnoughFoundException("");
        }
        getCurrentLocation().setOwner(this);
        funding -= location.getPrice();
    }

    public int getFunding() {
        return funding;
    }
}
