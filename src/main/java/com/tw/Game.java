package com.tw;

import com.tw.exception.IllegalPlayerSettingException;
import com.tw.exception.RichGameException;
import com.tw.generator.DefaultMap;
import com.tw.generator.GameMap;
import com.tw.location.Location;
import com.tw.util.RelativeIndex;
import com.tw.util.Tool;

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
    private List<Location> locations = new ArrayList<>();
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

    public void setPlayerToNext() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        if (!getCurrentPlayer().canRoll()) {
            getCurrentPlayer().decreaseSkipRoll();
            getCurrentPlayer().decreaseUnpunishRoll();
            setPlayerToNext();
        }
    }

    private int getPlayerIndex(Player player) {
        return players.indexOf(player);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void roll() {
        getCurrentPlayer().roll();
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

    public void removePlayer(Player player) {
        players.remove(player);
        player.cleanLand();
        currentPlayerIndex--;
    }

    public boolean isOver() {
        return players.size() == 1;
    }

    public void useTool(Tool tool, int relativeIndex) throws RichGameException {
        getCurrentPlayer().useTool(tool, relativeIndex);
    }

    public Location getRelativeLocationWith(Player player, int relativeIndex) {
        return getLocation(RelativeIndex.get(player.getLocationIndex(), relativeIndex, getMapSize()));
    }

    public int getHospitalIndex() {
        return map.getHospitalIndex();
    }

    public void useRobot() throws RichGameException {
        getCurrentPlayer().robot();
    }
}
