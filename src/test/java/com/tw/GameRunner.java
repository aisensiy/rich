package com.tw;

import com.tw.exception.RichGameException;
import com.tw.location.Location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GameRunner {

    private Game game;

    public GameRunner(Game game) {
        this.game = game;
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
            game.setInitFunding(Integer.parseInt(fundingString));
        }
    }

    public void setPlayers() throws IOException, RichGameException {
        System.out.print("请选择2~4位不重复玩家，输入编号即可。(1.钱夫人; 2.阿土伯; 3.孙小美; 4.金贝贝):");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String playerString = br.readLine();
        if (!playerString.isEmpty()) {
            game.setPlayers(playerString);
        }
    }


    public String display() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 29; i++) {
            sb.append(getSymbol(game.getLocation(i)));
        }
        sb.append("\n");
        int size = game.getLocationSize();
        for (int i = 0; i < 6; i++) {
            sb.append(getSymbol(game.getLocation(size - i - 1)));
            sb.append("                           ");
            sb.append(getSymbol(game.getLocation(29 + i)));
            sb.append("\n");
        }
        for (int i = 63; i > 63 - 29; i--) {
            sb.append(getSymbol(game.getLocation(i)));
        }
        return sb.toString();
    }

    private String getSymbol(Location location) {
        Player playerAtLocation = game.getPlayerAtLocation(location);
        if (playerAtLocation != null) {
            return playerAtLocation.getSymbol();
        } else {
            return location.getSymbol();
        }
    }
}
