package com.tw.map;

import com.tw.generator.GameMap;
import com.tw.location.Prison;

import java.util.ArrayList;

public class PrisonMap extends GameMap {
    @Override
    public void init() {
        locations = new ArrayList<>();
        locations.add(new Prison());
    }

    @Override
    public String display() {
        return "P";
    }
}
