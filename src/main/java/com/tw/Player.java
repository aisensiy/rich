package com.tw;

import com.tw.exception.CannotAccessLandException;
import com.tw.exception.CannotBuyToolException;
import com.tw.exception.NoEnoughFoundException;
import com.tw.exception.RichGameException;
import com.tw.location.Land;
import com.tw.location.Location;
import com.tw.util.Dice;
import com.tw.util.RelativeIndex;
import com.tw.util.Tool;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.tw.util.Tool.BOMB;
import static com.tw.util.Tool.ROADBLOCK;
import static com.tw.util.Tool.ROBOT;

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
    private String symbol;
    private List<Land> lands = new ArrayList<>();
    private int skipRoll = 0;
    private int unpunishRoll = 0;
    private List<Tool> tools = new ArrayList<>();

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
        return game.getLocation(currrentLocationIndex);
    }

    public int getFunding() {
        return funding;
    }

    public void decreaseFunding(int price) {
        funding -= price;
    }

    public int getPoint() {
        return point;
    }

    public void increasePoint(int point) {
        this.point += point;
    }

    public int getToolCount() {
        return tools.size();
    }

    public void roll() {
        int step = Dice.roll();
        System.out.println(String.format("掷骰子点数%d", step));
        for (int i = 1; i <= step; i++) {
            go(1);
            if (triggerPassEvent(getCurrentLocation())) break;
        }
        decreaseUnpunishRoll();
    }

    private boolean triggerPassEvent(Location location) {
        boolean stop = false;
        if (location.getTool() != null) {
            stop = location.getTool().triggerPassEvent(this);
            location.setTool(null);
        }
        return stop;
    }

    public int getCountOf(Tool tool) {
        return (int) tools.stream().filter(t -> t == tool).count();
    }

    public void addTool(Tool tool) {
        tools.add(tool);
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
                getToolInfo());
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

    public void setSkipTurn(int n) {
        skipRoll = n;
    }

    public boolean hasPointBuyTool() {
        return point >= Stream.of(Tool.values()).mapToInt(Tool::getPrice).min().getAsInt();
    }

    public void decreaseTool(Tool tool) {
        tools.remove(tool);
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

    public void setUnpunishTurn(int roll) {
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
        land.reset();
        increaseBy(land.getPrice());
        removeLand(land);
    }

    public String getToolInfo() {
        return String.format("道具: 路障%d个；炸弹%d个；机器娃娃%d个", getCountOf(ROADBLOCK), getCountOf(BOMB), getCountOf(ROBOT));
    }

    public void buyLand(Land land) throws RichGameException {
        if (land.getOwner() != null) {
            throw new CannotAccessLandException("current location already get an owner");
        }

        ensureFoundingIsEnough(land.getLandPrice());
        land.setOwner(this);
        addLand(land);
        decreaseFunding(land.getLandPrice());
    }

    public void upgradeLand(Land land) throws RichGameException {
        if (land.getOwner() != this) {
            throw new CannotAccessLandException("can not upgrade land which is not belong to you");
        }

        if (land.isHighestLevel()) {
            throw new CannotAccessLandException("can not upgrade land with highest level");
        }

        ensureFoundingIsEnough(land.getLandPrice());
        decreaseFunding(land.getLandPrice());
        land.upgradeLevel();
    }

    private void ensureFoundingIsEnough(int price) throws NoEnoughFoundException {
        if (getFunding() < price) {
            throw new NoEnoughFoundException("没有足够的金钱");
        }
    }

    private void removeLand(Land land) {
        lands.remove(land);
    }

    public String getColor() {
        return color;
    }

    public void cleanLand() {
        for (Land land : lands) {
            land.reset();
        }
    }

    public void buyTool(Tool tool) throws RichGameException {
        if (getToolCount() >= 10) {
            throw new CannotBuyToolException("the player already get 10 tools");
        }
        if (getPoint() < tool.getPrice()) {
            throw new CannotBuyToolException("no enough point to buy the tool");
        }
        addTool(tool);
        point -= tool.getPrice();
    }
}
