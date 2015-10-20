package com.tw.location;

import com.tw.Player;
import com.tw.util.Tool;
import com.tw.exception.CannotBuyToolException;
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
    public String getLocationSymbol() {
        return "T";
    }

    public void buy(Player player, Tool tool) throws RichGameException {
        if (player.getToolCount() >= 10) {
            throw new CannotBuyToolException("the player already get 10 tools");
        }
        if (player.getPoint() < tool.getPrice()) {
            throw new CannotBuyToolException("no enough point to buy the tool");
        }
        player.addTool(tool);
        player.decreasePoint(tool.getPrice());
    }

    public String showTools() {
        return Tool.listTools();
    }
}
