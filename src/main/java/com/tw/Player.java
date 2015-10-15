package com.tw;

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
    private int point;

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

    public Location getCurrentLocation() {
        return game.location(currrentLocationIndex);
    }

    public void buyEmptyLand() throws RichGameException {
        Location location = game.location(currrentLocationIndex);
        location.process(this);
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
        Location location = game.location(currrentLocationIndex);
        location.process(this);
    }

    private Land castToLand(Location location) throws TypeCastException {
        if (!location.isLand()) {
            throw new TypeCastException("can not convert other type to land");
        }
        return (Land) location;
    }

    public void decreaseBy(int price) {
        funding -= price;
    }

    public int getPoint() {
        return point;
    }

    public void increasePoint(int point) {
        this.point += point;
    }
}
