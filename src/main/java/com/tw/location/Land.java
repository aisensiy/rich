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
        if (getOwner() != null && getOwner() != player) {
            if (!getOwner().canRoll()) {
                System.out.println("由于土地拥有者在监狱或者医院，不惩罚");
            } else if (!player.canPunish()) {
                System.out.println("由于福神附身，不惩罚");
            } else {
                getOwner().getTollFrom(player, punish());
                System.out.println(String.format("到达%s的%s,损失金钱%d元", getOwner().getName(), getName(), punish()));
            }
        }
    }

    private int punish() {
        return price / 2 * (level + 1);
    }

    @Override
    protected String getLocationSymbol() {
        if (getOwner() != null) {
            return getOwner().getColor() + Integer.toString(level) + Player.ANSI_RESET;
        } else {
            return Integer.toString(level);
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
