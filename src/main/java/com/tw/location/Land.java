package com.tw.location;

import com.tw.Player;
import com.tw.exception.CannotAccessLandException;
import com.tw.exception.NoEnoughFoundException;
import com.tw.exception.RichGameException;

public class Land extends Location {
    public static final int EMPTY_LAND = 0;
    private int price;
    private int level;

    public Land(int price) {
        super();
        this.price = price;
        level = 0;
    }

    public int getPrice() {
        return price;
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
    public void process(Player player) throws RichGameException {
        if (getOwner() == null) {
            buy(player);
        } else {
            upgrade(player);
        }
    }

    @Override
    public String getSymbol() {
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
        player.addEmptyLand(this);
        player.decreaseBy(price);
    }

    private void ensureFoundingIsEnough(Player player) throws NoEnoughFoundException {
        if (player.getFunding() < price) {
            throw new NoEnoughFoundException("");
        }
    }

    public boolean isHighestLevel() {
        return level == 3;
    }
}
