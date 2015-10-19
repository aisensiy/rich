package com.tw.location;

import com.tw.Player;
import com.tw.exception.RichGameException;

public class Mine extends Location {
    private int point;

    public Mine(int point) {
        this.point = point;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void triggerArriveEvent(Player player) throws RichGameException {
        player.increasePoint(point);
    }

    @Override
    public String getSymbol() {
        return "$";
    }

    @Override
    public String arriveMessage(Player player) {
        return String.format("在矿点获取点数" + point);
    }
}
