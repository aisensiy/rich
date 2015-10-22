package com.tw.location;

import com.tw.Player;
import com.tw.exception.PlayerIsOutException;
import com.tw.util.Tool;
import com.tw.exception.RichGameException;

import static com.tw.util.Tool.BOMB;

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

    public void triggerArriveEvent(Player player) throws RichGameException, PlayerIsOutException {

    }

    public void process(Player player) throws RichGameException, PlayerIsOutException {
        boolean stopProess = triggerArriveToolEvent(player);
        if (stopProess) {
            return;
        }
        triggerArriveEvent(player);
        if (arriveMessage(player) != null || !arriveMessage(player).isEmpty())
            System.out.println(arriveMessage(player));
    }

    private boolean triggerArriveToolEvent(Player player) {
        boolean stop = false;
        if (tool != null)
            stop = tool.triggerArriveEvent(player);
        tool = null;
        return stop;
    }

    public String getSymbol() {
        if (tool != null) {
            return tool.getSymbol();
        } else {
            return getLocationSymbol();
        }
    }

    protected abstract String getLocationSymbol();

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

    public void addTool(Tool tool) throws RichGameException {
        ensureNoToolOnLocation(this);
        this.tool = tool;
    }

    public boolean isGiftShop() {
        return this.getClass() == GiftShop.class;
    }

    public void reset() {

    }

    private void ensureNoToolOnLocation(Location location) throws RichGameException {
        if (location.getTool() != null) {
            throw new RichGameException("there is already a tool on the location");
        }
    }

    public void removeTool() {
        tool = null;
    }
}
