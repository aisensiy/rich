package com.tw;

import com.tw.exception.RichGameException;

public class StartPoint extends Location {
    @Override
    public String getName() {
        return "起点";
    }

    @Override
    public void process(Player player) throws RichGameException {

    }

    @Override
    public String getSymbol() {
        return "S";
    }
}
