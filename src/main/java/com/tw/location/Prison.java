package com.tw.location;

import com.tw.Player;
import com.tw.exception.RichGameException;

public class Prison extends Location {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void triggerArriveEvent(Player player) throws RichGameException {
        player.setSkipRoll(3);
    }

    @Override
    public String getLocationSymbol() {
        return "P";
    }

    @Override
    public String arriveMessage(Player player) {
        return String.format("%s被送去监狱了", player.getName());
    }
}
