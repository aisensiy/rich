package com.tw.location;

import com.tw.Player;
import com.tw.exception.RichGameException;

public class Prison extends Location {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void process(Player player) throws RichGameException {
        player.setSkipRoll(3);
    }

    @Override
    public String getSymbol() {
        return "P";
    }
}
