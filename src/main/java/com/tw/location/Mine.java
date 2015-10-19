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
    public void process(Player player) throws RichGameException {
        player.increasePoint(point);
        System.out.println(getMessage());
    }

    @Override
    public String getSymbol() {
        return "$";
    }

    @Override
    public String getMessage() {
        return String.format("在矿点获取点数" + point);
    }
}
