package com.tw.location;

import com.tw.Player;
import com.tw.exception.CannotAccessLandException;
import com.tw.exception.NoEnoughFoundException;
import com.tw.exception.RichGameException;

public class Land extends Location {
    public static final int EMPTY_LAND = 0;
    public static final int LEVEL_ONE = 1;
    public static final int LEVEL_TWO = 2;
    public static final int LEVEL_THREE = 3;
    private int price;
    private int level;

    public Land(int price) {
        super();
        this.price = price;
        level = 0;
    }

    public int getPrice() {
        return price * (level + 1);
    }

    public int getLevel() {
        return level;
    }

    public void upgradeLevel() {
        level++;
    }

    @Override
    public String getName() {
        switch (level) {
            case 1:
                return "茅屋";
            case 2:
                return "洋房";
            case 3:
                return "摩天楼";
            default:
                return "空地";
        }
    }

    @Override
    public void triggerArriveEvent(Player player) throws RichGameException {
        if (getOwner() == null) {
            buy(player);
        } else if(getOwner() == player) {
            upgrade(player);
        } else if (getOwner().canRoll() && player.canPunish()) {
            player.decreaseBy(punish());
            getOwner().increaseBy(punish());
            System.out.println(String.format("到达%s的%s,损失金钱%d元", getOwner().getName(), getName(), punish()));
        }
    }

    private int punish() {
        return price / 2 * (level + 1);
    }

    @Override
    protected String getLocationSymbol() {
        return Integer.toString(level);
    }

    private void upgrade(Player player) throws RichGameException {
        if (getOwner() != player) {
            throw new CannotAccessLandException("can not upgrade land which is not belong to you");
        }

        if (isHighestLevel()) {
            throw new CannotAccessLandException("can not upgrade land with highest level");
        }

        ensureFoundingIsEnough(player);
        player.decreaseBy(price);
        upgradeLevel();
    }

    private void buy(Player player) throws RichGameException {
        if (getOwner() != null) {
            throw new CannotAccessLandException("current location already get an owner");
        }

        ensureFoundingIsEnough(player);
        setOwner(player);
        player.addLand(this);
        player.decreaseBy(price);
    }

    private void ensureFoundingIsEnough(Player player) throws NoEnoughFoundException {
        if (player.getFunding() < price) {
            throw new NoEnoughFoundException("没有足够的金钱");
        }
    }

    public boolean isHighestLevel() {
        return level == 3;
    }

    @Override
    public void reset() {
        setOwner(null);
        level = 0;
    }

    public int getLandPrice() {
        return price;
    }
}
