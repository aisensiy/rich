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

    public void buy(Player player, Tool tool) {
        player.addTool(tool);
        player.decreasePoint(tool.getPrice());
    }
}
