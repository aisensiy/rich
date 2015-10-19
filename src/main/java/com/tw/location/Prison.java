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
        System.out.println(String.format("%s被送去监狱了", player.getName()));
    }

    @Override
    public String getSymbol() {
        return "P";
    }
}
