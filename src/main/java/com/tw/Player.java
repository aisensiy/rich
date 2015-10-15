package com.tw;

public class Player {
    private final String name;
    private static final Player[] players = new Player[] {
        new Player("钱夫人"),
        new Player("阿土伯"),
        new Player("孙小美"),
        new Player("金贝贝")
    };
    private int currentLocation = 0;

    private Player(String name) {
        this.name = name;
    }

    public static Player createPlayer(int index) {
        Player player = players[index];
        player.resetLocation();
        return player;
    }

    public String getName() {
        return name;
    }

    public int getLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(int currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void go(int step) {
        currentLocation = (currentLocation + step) % Game.MAP_SIZE;
    }

    public void resetLocation() {
        currentLocation = 0;
    }
}
