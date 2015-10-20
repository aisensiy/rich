package com.tw;

import com.tw.exception.RichGameException;
import com.tw.location.Land;
import com.tw.location.Location;
import com.tw.util.RelativeIndex;
import com.tw.util.Tool;
import com.tw.util.ToolBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Player {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private final String name;
    private static final String[] playerNames = new String[]{
            "钱夫人",
            "阿土伯",
            "孙小美",
            "金贝贝"
    };
    private static final String[] playerSymbols = new String[]{
            "Q",
            "A",
            "S",
            "J"
    };
    private static final String[] colors = new String[] {
            ANSI_RED,
            ANSI_GREEN,
            ANSI_YELLOW,
            ANSI_BLUE
    };
    private int currrentLocationIndex = 0;
    private Game game;
    private int funding;
    private int point = 0;
    private String color;
    private ToolBox toolBox = new ToolBox(this);
    private String symbol;
    private List<Land> lands = new ArrayList<>();
    private int skipRoll = 0;
    private int unpunishRoll = 0;

    private Player(Game game, String name, String symbol, String color, int funding) {
        this.game = game;
        this.name = name;
        this.symbol = symbol;
        this.color = color;
        this.funding = funding;
    }

    public static Player createPlayer(Game game, int index) {
        Player player = createPlayer(game, index, Game.DEFAULT_FUNDING);
        return player;
    }

    public static Player createPlayer(Game game, int index, int funding) {
        Player player = new Player(game, playerNames[index - 1], playerSymbols[index - 1], colors[index - 1], funding);
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
        currrentLocationIndex = RelativeIndex.get(currrentLocationIndex, step, game.getMapSize());
    }

    public Location getCurrentLocation() {
        return game.location(currrentLocationIndex);
    }

    public int getFunding() {
        return funding;
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

    public int getToolCount() {
        return toolBox.getToolCount();
    }

    public int getCountOf(Tool tool) {
        return toolBox.getCountOf(tool);
    }

    public void addTool(Tool tool) {
        toolBox.addTool(tool);
    }

    public void decreasePoint(int toolPoint) {
        point -= toolPoint;
    }

    public String getSymbol() {
        return color + symbol + ANSI_RESET;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", name, symbol);
    }

    public String getInfo() {
        return String.format(
                "资金: %d元\n" +
                "点数: %d点\n" +
                "%s\n" +
                "%s",
                funding, point,
                getLandInfo(),
                toolBox);
    }

    public String getLandInfo() {
        return String.format("地产: 空地%d处；茅屋%d处；洋房%d处；摩天楼%d处",
                countOfLandWithLevel(Land.EMPTY_LAND),
                countOfLandWithLevel(Land.LEVEL_ONE),
                countOfLandWithLevel(Land.LEVEL_TWO),
                countOfLandWithLevel(Land.LEVEL_THREE));
    }

    public void addLand(Land land) {
        lands.add(land);
    }

    public int countOfLandWithLevel(int level) {
        return (int) lands.stream().filter(land -> land.getLevel() == level).count();
    }

    public boolean canRoll() {
        return skipRoll == 0;
    }

    public void decreaseSkipRoll() {
        if (skipRoll > 0)
            skipRoll--;
    }

    public void setSkipRoll(int n) {
        skipRoll = n;
    }

    public boolean hasPointBuyTool() {
        return point >= Stream.of(Tool.values()).mapToInt(Tool::getPrice).min().getAsInt();
    }

    public void decreaseTool(Tool tool) {
        toolBox.remoteTool(tool);
    }

    public int getSkipRoll() {
        return skipRoll;
    }

    public void goToHospital() {
        currrentLocationIndex = game.getHospitalIndex();
        skipRoll = 3;
    }

    public void robot() throws RichGameException {
        if (getCountOf(Tool.ROBOT) == 0) {
            throw new RichGameException("no such tool");
        }
        for (int i = 1; i <= 10; i++) {
            Location location = game.getRelativeLocationWith(this, i);
            location.setTool(null);
        }
        this.decreaseTool(Tool.ROBOT);
    }

    public void increaseBy(int money) {
        funding += money;
    }

    public void getGod() {
        unpunishRoll = 5;
    }

    public void setUnpunishRoll(int roll) {
        unpunishRoll = roll;
    }

    public boolean canPunish() {
        return unpunishRoll == 0;
    }

    public void decreaseUnpunishRoll() {
        if (unpunishRoll > 0)
            unpunishRoll--;
    }

    public int getUnpunishRoll() {
        return unpunishRoll;
    }

    public void sellTool(Tool tool) throws RichGameException {
        if (getCountOf(tool) == 0) {
            throw new RichGameException("no such tool");
        }
        decreaseTool(tool);
        increasePoint(tool.getPrice());
    }

    public void sell(int index) throws RichGameException {
        if (!game.getLocation(index).isLand() || lands.indexOf(game.getLocation(index)) == -1) {
            throw new RichGameException("no such land");
        }
        Land land = (Land) game.getLocation(index);
        land.setOwner(null);
        increaseBy(land.getPrice());
        removeLand(land);
    }

    private void removeLand(Land land) {
        lands.remove(land);
    }

    public String getColor() {
        return color;
    }
}
