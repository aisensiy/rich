package com.tw.location;

import com.tw.Player;
import com.tw.exception.RichGameException;

public class Magic extends Location {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void process(Player player) throws RichGameException {

    }

    @Override
    public String getLocationSymbol() {
        return "M";
    }
}
