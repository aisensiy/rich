package com.tw;

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
    public void process(Player player) throws RichGameException {
        player.increasePoint(point);
    }

    @Override
    public String getSymbol() {
        return "$";
    }
}
