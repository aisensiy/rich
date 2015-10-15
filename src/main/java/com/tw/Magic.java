package com.tw;

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
    public String getSymbol() {
        return "M";
    }
}
