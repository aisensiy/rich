package com.tw.map;

import com.tw.generator.GameMap;
import com.tw.location.Hospital;
import com.tw.location.Location;

import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class HospitalMap extends GameMap {
    @Override
    public void init() {
        locations = asList(new Hospital(), new Hospital(), new Hospital());
    }

    @Override
    public String display() {
        return locations.stream().map(Location::getSymbol).collect(Collectors.joining(""));
    }
}
