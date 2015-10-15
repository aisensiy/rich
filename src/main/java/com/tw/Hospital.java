package com.tw;

import com.tw.exception.RichGameException;

public class Hospital extends Location {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void process(Player player) throws RichGameException {

    }

    @Override
    public String getSymbol() {
        return "H";
    }
}
