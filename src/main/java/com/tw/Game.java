package com.tw;

import com.tw.exception.IllegalPlayerSettingException;
import com.tw.exception.RichGameException;

import java.util.stream.Stream;

public class Game {
    public static final int DEFAULT_FUNDING = 10000;
    public static final int MAP_SIZE = 70;

    public int initFunding = DEFAULT_FUNDING;
    private String playersString;
    private Player[] players;
    private Player player;

    public void setInitFunding(int initFunding) {
        this.initFunding = initFunding;
    }

    public Player getPlayer(int idx) {
        return players[idx - 1];
    }

    public void setPlayers(String playersString) throws RichGameException {
        if (playersString.length() < 2 || playersString.length() > 4) {
            throw new IllegalPlayerSettingException("2 ~ 4 players required");
        }
        players = Stream.of(playersString.split(""))
                .map(Integer::parseInt)
                .map(i -> Player.createPlayer(this, i - 1))
                .toArray(size -> new Player[size]);
    }
}
