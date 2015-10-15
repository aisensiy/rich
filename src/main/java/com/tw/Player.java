package com.tw;

public class Player {
    private final String name;
    private static final Player[] players = new Player[] {
        new Player("钱夫人"),
        new Player("阿土伯"),
        new Player("孙小美"),
        new Player("金贝贝")
    };

    private Player(String name) {
        this.name = name;
    }

    public static Player createPlayer(int index) {
        return players[index];
    }

    public String getName() {
        return name;
    }
}
