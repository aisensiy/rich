package com.tw.map;

import com.tw.generator.GameMap;
import com.tw.location.Mine;
import com.tw.location.ToolShop;

import java.util.ArrayList;

public class ToolShopMap extends GameMap {

    @Override
    public void init() {
        locations = new ArrayList<>();
        locations.add(new Mine(10000));
        locations.add(new Mine(10000));
        locations.add(new ToolShop());
    }

    @Override
    public String display() {
        return "T";
    }
}
