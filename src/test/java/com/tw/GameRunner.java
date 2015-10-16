package com.tw;

import com.tw.exception.RichGameException;

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
        String command = readLine().toLowerCase();
        while (true) {
            if (command.equals("roll")) {
                turn();
            } else if (command.equals("query")) {
                System.out.println(game.getCurrentPlayer().getInfo());
            } else if (command.equals("help")) {
                System.out.println(help());
            } else {
                break;
            }
            command = readLine();
        }
    }

    public void turn() throws IOException {
        System.out.println(display());
        System.out.print(String.format("%s> ", game.getCurrentPlayer()));
        game.roll();
    }

    void setInitFunding() throws IOException {
        System.out.print("Set init funding: 1000 ~ 50000, default 10000 > ");

        String fundingString;
        fundingString = readLine();
        if (!fundingString.isEmpty()) {
            game.setInitFunding(Integer.parseInt(fundingString));
        }
    }

    public void setPlayers() throws IOException, RichGameException {
        System.out.print("请选择2~4位不重复玩家，输入编号即可。(1.钱夫人; 2.阿土伯; 3.孙小美; 4.金贝贝): ");

        String playerString = readLine();
        if (!playerString.isEmpty()) {
            game.setPlayers(playerString);
        }
    }

    public String display() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 29; i++) {
            sb.append(game.getSymbol(game.getLocation(i)));
        }
        sb.append("\n");
        int size = game.getLocationSize();
        for (int i = 0; i < 6; i++) {
            sb.append(game.getSymbol(game.getLocation(size - i - 1)));
            sb.append("                           ");
            sb.append(game.getSymbol(game.getLocation(29 + i)));
            sb.append("\n");
        }
        for (int i = 63; i > 63 - 29; i--) {
            sb.append(game.getSymbol(game.getLocation(i)));
        }
        return sb.toString();
    }


    private String readLine() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }

    public static void main(String... args) throws IOException, RichGameException {
        new GameRunner(new Game()).run();
    }

    public String help() {
        return "命令一览表\n" +
                "roll           掷骰子命令，行走1~6步。步数由随即算法产生。   \n" +
                "block n     玩家拥有路障后，可将路障放置到离当前位置前后10步的距离，任一玩家经过路障，都将被拦截。该道具一次有效。n 前后的相对距离，负数表示后方。\n" +
                "bomb n    可将路障放置到离当前位置前后10步的距离，任一玩家j 经过在该位置，将被炸伤，送往医院，住院三天。n 前后的相对距离，负数表示后方。\n" +
                "robot        使用该道具，可清扫前方路面上10步以内的其它道具，如炸弹、路障。\n" +
                "sell x        出售自己的房产，x 地图上的绝对位置，即地产的编号。\n" +
                "sellTool x  出售道具，x 道具编号\n" +
                "query        显示自家资产信息   \n" +
                "help          查看命令帮助   \n" +
                "quit           强制退出";
    }
}
