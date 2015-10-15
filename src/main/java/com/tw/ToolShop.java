package com.tw;

import com.tw.exception.RichGameException;

public class ToolShop extends Location {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void process(Player player) throws RichGameException {

    }

    public void buy(Player player, int idx) {
        int price = 0;
        switch (idx) {
            case 1: price = 50; break;
            case 2: price = 30; break;
        }
        player.addTool(idx);
        player.decreasePoint(price);
    }
}
