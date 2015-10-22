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

    public String showTools() {
        return Tool.listTools();
    }
}
