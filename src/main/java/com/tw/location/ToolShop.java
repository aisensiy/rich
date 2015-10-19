package com.tw.location;

import com.tw.Player;
import com.tw.Tool;
import com.tw.exception.RichGameException;

public class ToolShop extends Location {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void process(Player player) throws RichGameException {

    }

    @Override
    public String getSymbol() {
        return "T";
    }

    public void buy(Player player, Tool tool) {
        player.addTool(tool);
        player.decreasePoint(tool.getPrice());
    }

    public String showList() {
        return null;
    }
}
