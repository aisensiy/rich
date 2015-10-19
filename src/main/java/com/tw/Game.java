package com.tw;

import com.tw.exception.IllegalPlayerSettingException;
import com.tw.exception.RichGameException;
import com.tw.generator.DefaultMap;
import com.tw.generator.GameMap;
import com.tw.location.Location;
import com.tw.util.Dice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Game {
    public static final int DEFAULT_FUNDING = 10000;
    public int initFunding = DEFAULT_FUNDING;

    private List<Player> players;
    private int currentPlayerIndex;
    private Player currentPlayer;
    private List<Location> locations = new ArrayList<>();
    private Dice dice = new Dice();
    private GameMap map;

    public Game() {
        this.map = new DefaultMap(this);
        this.locations = map.getLocations();
    }

    public Game(GameMap map) {
        this.map = map;
        this.map.setGame(this);
        this.locations = map.getLocations();
    }

    public void setInitFunding(int initFunding) {
        this.initFunding = initFunding;
    }

    public Player getPlayer(int idx) {
        return players.get(idx - 1);
    }

    public void setPlayers(String playersString) throws RichGameException {
        if (playersString.length() < 2 || playersString.length() > 4) {
            throw new IllegalPlayerSettingException("2 ~ 4 players required");
        }

        ensureValidPlayersString(playersString);

        players = Stream.of(playersString.split(""))
                .map(Integer::parseInt)
                .map(i -> Player.createPlayer(this, i, initFunding))
                .collect(toList());

        currentPlayerIndex = 0;
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
        if (getCurrentPlayer().getCurrentLocation() == location) {
            return getCurrentPlayer();
        }

        Player player = nextPlayer(getCurrentPlayer());
        while (player != getCurrentPlayer()) {
            if (player.getCurrentLocation() == location) {
                return player;
            }
            player = nextPlayer(player);
        }
        return null;
    }

    public Player nextPlayer(Player player) {
        int currentPlayerIndex = getPlayerIndex(player);
        return players.get((currentPlayerIndex + 1) % players.size());
    }

    public void setCurrentPlayerToNext() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        if (!getCurrentPlayer().canRoll()) {
            getCurrentPlayer().decreaseSkipRoll();
            setCurrentPlayerToNext();
        }
    }



    private int getCurrentPlayerIndex() {
        return getPlayerIndex(currentPlayer);
    }

    private int getPlayerIndex(Player player) {
        return players.indexOf(player);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void roll() {
        getCurrentPlayer().go(dice.getInt());
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public int getMapSize() {
        return locations.size();
    }

    public Location getLocation(int idx) {
        return locations.get(idx);
    }

    public String getSymbol(Location location) {
        Player playerAtLocation = getPlayerAtLocation(location);
        if (playerAtLocation != null) {
            return playerAtLocation.getSymbol();
        } else {
            return location.getSymbol();
        }
    }

    public String display() {
        return map.display();
    }

    public int forward(Player player, int step) {
        return (player.getLocationIndex() + step) % getMapSize();
    }

    public void removePlayer(Player player) {
        players.remove(player);
        currentPlayerIndex--;
    }

    public boolean isOver() {
        return players.size() == 1;
    }

    public void setBlock(int relativeIndex) throws RichGameException {
        if (getCurrentPlayer().getCountOfRoadBlock() == 0) {
            throw new RichGameException("no such tool");
        }
        getRelativeLocationWithCurrent(relativeIndex).setTool(Tool.ROADBLOCK);
        getCurrentPlayer().decreaseTool(Tool.ROADBLOCK);
    }

    public void setBomb(int relativeIndex) {
        getRelativeLocationWithCurrent(relativeIndex).setTool(Tool.BOMB);
        getCurrentPlayer().decreaseTool(Tool.BOMB);
    }

    public Location getRelativeLocationWithCurrent(int relativeIndex) {
        return getLocation(currentPlayerIndex + relativeIndex);
    }
}
