package com.tw.map;

import com.tw.generator.GameMap;
import com.tw.location.GiftShop;

import java.util.ArrayList;

public class GiftMap extends GameMap {
    @Override
    public void init() {
        locations = new ArrayList<>();
        locations.add(new GiftShop());
    }

    @Override
    public String display() {
        return "G";
    }
}
