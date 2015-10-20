package com.tw.location;

import com.tw.Player;
import com.tw.Tool;
import com.tw.exception.RichGameException;

import static com.tw.Tool.BOMB;

public abstract class Location {

    private Player owner;
    private Tool tool;

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public boolean isLand() {
        return this.getClass() == Land.class;
    }

    public abstract String getName();

    public void triggerArriveEvent(Player player) throws RichGameException {

    }

    public void process(Player player) throws RichGameException {
        if (tool == BOMB) {
            player.goToHospital();
            tool = null;
            System.out.println(String.format("%s踩到了炸弹，被送往了医院", player.getName()));
            return;
        }
        triggerArriveEvent(player);
        System.out.println(arriveMessage(player));
    }

    public abstract String getSymbol();

    public boolean isEmptyLand() {
        if (!isLand()) {
            return false;
        }
        Land land = (Land) this;
        return land.getOwner() == null;
    }

    public String arriveMessage(Player player) {
        return "";
    };

    public boolean isToolShop() {
        return this.getClass() == ToolShop.class;
    }

    public Tool getTool() {
        return tool;
    }

    public void setTool(Tool tool) {
        this.tool = tool;
    }

    public boolean isGiftShop() {
        return this.getClass() == GiftShop.class;
    }

    public void reset() {

    }
}
