package com.tw;

import com.tw.exception.CannotBuyToolException;
import com.tw.exception.NoEnoughFoundException;
import com.tw.exception.RichGameException;
import com.tw.exception.TypeCastException;
import com.tw.location.Land;
import com.tw.location.Location;
import com.tw.location.ToolShop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tw.Tool.*;

public class Player {
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
    private int currrentLocationIndex = 0;
    private Game game;
    private int funding;
    private int point;
    private Map<Tool, Integer> toolMap = new HashMap<>();
    private String symbol;
    private Map<Integer, List<Location>> landMap = new HashMap<>();

    private Player(Game game, String name, String symbol, int funding) {
        this.game = game;
        this.name = name;
        this.symbol = symbol;
        this.funding = funding;
    }

    public static Player createPlayer(Game game, int index) {
        Player player = createPlayer(game, index, Game.DEFAULT_FUNDING);
        return player;
    }

    public static Player createPlayer(Game game, int index, int funding) {
        Player player = new Player(game, playerNames[index - 1], playerSymbols[index - 1], funding);
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
        currrentLocationIndex = game.forward(this, step);
    }

    public Location getCurrentLocation() {
        return game.location(currrentLocationIndex);
    }

    public void buyEmptyLand() throws RichGameException {
        Location location = game.location(currrentLocationIndex);
        location.process(this);
    }

    private void ensureFoundingIsEnough(int price) throws NoEnoughFoundException {
        if (funding < price) {
            throw new NoEnoughFoundException("");
        }
    }

    public int getFunding() {
        return funding;
    }

    public void upgradeLand() throws RichGameException {
        Location location = game.location(currrentLocationIndex);
        location.process(this);
    }

    private Land castToLand(Location location) throws TypeCastException {
        if (!location.isLand()) {
            throw new TypeCastException("can not convert other type to land");
        }
        return (Land) location;
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

    public void buyTool(ToolShop toolShop, Tool tool) throws RichGameException {
        if (getToolCount() >= 10) {
            throw new CannotBuyToolException("the player already get 10 tools");
        }
        if (point < tool.getPrice()) {
            throw new CannotBuyToolException("no enough point to buy the tool");
        }
        toolShop.buy(this, tool);
    }

    private int getToolCount() {
        return getCountOfRoadBlock() + getCountOfRobot() + getCountOfBomb();
    }

    private int getCountOfBomb() {
        return toolMap.getOrDefault(BOMB, 0);
    }

    public int getCountOfRoadBlock() {
        return toolMap.getOrDefault(ROADBLOCK, 0);
    }

    public void addTool(Tool tool) {
        toolMap.put(tool, toolMap.getOrDefault(tool, 0) + 1);
    }

    public void decreasePoint(int toolPoint) {
        point -= toolPoint;
    }

    public int getCountOfRobot() {
        return toolMap.getOrDefault(ROBOT, 0);
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", name, symbol);
    }

    public String getInfo() {
        return String.format(
                "资金: %d元\n" +
                "点数: %d点\n" +
                "地产: 空地0处；茅屋0处；洋房0处；摩天楼0处\n" +
                "道具: 路障%d个；炸弹%d个；机器娃娃%d个",
                funding, point, getCountOfRoadBlock(), getCountOfBomb(), getCountOfRobot());
    }

    public int countOfEmptyLand() {
        return landMap.getOrDefault(Land.EMPTY_LAND, new ArrayList<>()).size();
    }

    public void addEmptyLand(Land land) {
        List<Location> locations = landMap.getOrDefault(Land.EMPTY_LAND, new ArrayList<>());
        locations.add(land);
        landMap.put(Land.EMPTY_LAND, locations);
    }
}
