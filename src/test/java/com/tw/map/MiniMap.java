package com.tw.map;

import com.tw.generator.GameMap;
import com.tw.location.*;

import java.util.ArrayList;

public class MiniMap extends GameMap {
    @Override
    public void init() {
        locations = new ArrayList<>();
        locations.add(new StartPoint());
        locations.add(new Mine(100));
        locations.add(new ToolShop());
        locations.add(new Mine(50));
        locations.add(new Land(50));
        locations.add(new Hospital());
    }

    @Override
    public String display() {
        return "S$T$0H";
    }
}
