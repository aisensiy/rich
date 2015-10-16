package com.tw.map;

import com.tw.generator.GameMap;
import com.tw.location.Mine;

import static java.util.Arrays.asList;

public class MineMap extends GameMap {
    @Override
    public void init() {
        locations = asList(new Mine(100));
    }

    @Override
    public String display() {
        return "$";
    }
}
