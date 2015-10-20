package com.tw.map;

import com.tw.generator.GameMap;
import com.tw.location.Land;
import com.tw.location.Location;

import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class Land200Map extends GameMap {
    @Override
    public void init() {
        locations = asList(new Land(200));
    }

    @Override
    public String display() {
        return locations.stream().map(Location::getSymbol).collect(Collectors.joining(""));
    }
}
