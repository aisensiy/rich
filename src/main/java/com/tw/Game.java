package com.tw;

import com.tw.exception.IllegalPlayerSettingException;
import com.tw.exception.RichGameException;
import com.tw.location.*;
import com.tw.util.Dice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Game {
    public static final int DEFAULT_FUNDING = 10000;
    public static final int MAP_SIZE = 70;
    public int initFunding = DEFAULT_FUNDING;

    private Player[] players;
    private Player currentPlayer;
    private List<Location> locations = new ArrayList<>();
    private Dice dice = new Dice();

    public Game() {
        initMap();
    }

    private void initMap() {
        locations.add(new StartPoint());
        for (int i = 0; i < 13; i++) {
            locations.add(new Land(200));
        }
        locations.add(new Hospital());
        for (int i = 0; i < 13; i++) {
            locations.add(new Land(200));
        }
        locations.add(new ToolShop());
        for (int i = 0; i < 6; i++) {
            locations.add(new Land(500));
        }
        locations.add(new GiftShop());
        for (int i = 0; i < 13; i++) {
            locations.add(new Land(300));
        }
        locations.add(new Prison());
        for (int i = 0; i < 13; i++) {
            locations.add(new Land(300));
        }
        locations.add(new Magic());
        locations.add(new ToolShop());

        locations.add(new Mine(20));
        locations.add(new Mine(80));
        locations.add(new Mine(100));
        locations.add(new Mine(40));
        locations.add(new Mine(80));
        locations.add(new Mine(60));
    }

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

        ensureValidPlayersString(playersString);

        players = Stream.of(playersString.split(""))
                .map(Integer::parseInt)
                .map(i -> Player.createPlayer(this, i, initFunding))
                .toArray(Player[]::new);

        currentPlayer = players[0];
    }

    private void ensureValidPlayersString(String playersString) throws IllegalPlayerSettingException {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < playersString.length(); i++) {
            int index = playersString.charAt(i) - '0';
            if (index > 4 || index < 1)
                throw new IllegalPlayerSettingException("1 ~ 4 number required");
            set.add(index);
        }
        if (set.size() != playersString.length()) {
            throw new IllegalPlayerSettingException("there should not be duplicated players");
        }
    }

    public Location location(int currrentLocationIndex) {
        return locations.get(currrentLocationIndex);
    }

    public Player getPlayerAtLocation(Location location) {
        if (currentPlayer.getCurrentLocation() == location) {
            return currentPlayer;
        }

        Player player = nextPlayer(currentPlayer);
        while (player != currentPlayer) {
            if (player.getCurrentLocation() == location) {
                return player;
            }
            player = nextPlayer(player);
        }
        return null;
    }

    public Player nextPlayer(Player player) {
        int currentPlayerIndex = getPlayerIndex(player);
        return players[(currentPlayerIndex + 1) % players.length];
    }

    public void setCurrentPlayerToNext() {
        currentPlayer = nextPlayer(currentPlayer);
    }

    private int getCurrentPlayerIndex() {
        return getPlayerIndex(currentPlayer);
    }

    private int getPlayerIndex(Player player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == player) {
                return i;
            }
        }
        return -1;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void roll() {
        int step = dice.getInt();
        currentPlayer.go(step);
        setCurrentPlayerToNext();
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public int getLocationSize() {
        return locations.size();
    }

    public Location getLocation(int idx) {
        return locations.get(idx);
    }
}
