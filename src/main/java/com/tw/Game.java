package com.tw;

import com.tw.exception.IllegalPlayerSettingException;
import com.tw.exception.RichGameException;
import com.tw.util.Dice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
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
        for (int i = 0; i < 6; i++) {
            locations.add(new Mine(10));
        }
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

    public void run() throws IOException, RichGameException {
        setInitFunding();
        setPlayers();
        System.out.println(display());
    }

    void setInitFunding() throws IOException {
        System.out.print("Set init funding: 1000 ~ 50000, default 10000 > ");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String fundingString = br.readLine();
        if (!fundingString.isEmpty()) {
            setInitFunding(Integer.parseInt(fundingString));
        }
    }

    public void setPlayers() throws IOException, RichGameException {
        System.out.print("请选择2~4位不重复玩家，输入编号即可。(1.钱夫人; 2.阿土伯; 3.孙小美; 4.金贝贝):");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String playerString = br.readLine();
        if (!playerString.isEmpty()) {
            setPlayers(playerString);
        }
    }

    public String display() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 29; i++) {
            sb.append(getSymbol(locations.get(i)));
        }
        sb.append("\n");
        int size = locations.size();
        for (int i = 0; i < 6; i++) {
            sb.append(getSymbol(locations.get(size - i - 1)));
            sb.append("                           ");
            sb.append(getSymbol(locations.get(29 + i)));
            sb.append("\n");
        }
        for (int i = 63; i > 63 - 29; i--) {
            sb.append(getSymbol(locations.get(i)));
        }
        return sb.toString();
    }

    private String getSymbol(Location location) {
        Player playerAtLocation = getPlayerAtLocation(location);
        if (playerAtLocation != null) {
            return playerAtLocation.getSymbol();
        } else {
            return location.getSymbol();
        }
    }

    private Player getPlayerAtLocation(Location location) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].getCurrentLocation() == location) {
                return players[i];
            }
        }
        return null;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void roll() {
        int step = dice.getInt();
        currentPlayer.go(step);
        for (int i = 0; i < players.length; i++) {
            if (players[i] == currentPlayer) {
                currentPlayer = players[(i + 1) % players.length];
                break;
            }
        }
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }
}
