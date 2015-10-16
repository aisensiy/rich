package com.tw.generator;

import com.tw.Game;
import com.tw.location.*;

import java.util.ArrayList;

public class DefaultMap extends GameMap {
    private Game game;

    public DefaultMap(Game game) {
        super();
        this.game = game;
    }

    @Override
    public void init() {
        locations = new ArrayList<>();

        locations.add(new StartPoint());
        for (int i = 0; i < 13; i++) {
            locations.add(new Land(200));
        }
        locations.add(new Hospital());
        for (int i = 0; i < 13; i++) {
            locations.add(new Land(200));
        }
        locations.add(new ToolShop());
        for (int i = 0; i < 6; i++) {
            locations.add(new Land(500));
        }
        locations.add(new GiftShop());
        for (int i = 0; i < 13; i++) {
            locations.add(new Land(300));
        }
        locations.add(new Prison());
        for (int i = 0; i < 13; i++) {
            locations.add(new Land(300));
        }
        locations.add(new Magic());
        locations.add(new ToolShop());

        locations.add(new Mine(20));
        locations.add(new Mine(80));
        locations.add(new Mine(100));
        locations.add(new Mine(40));
        locations.add(new Mine(80));
        locations.add(new Mine(60));
    }

    @Override
    public String display() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 29; i++) {
            sb.append(game.getSymbol(game.getLocation(i)));
        }
        sb.append("\n");
        int size = game.getMapSize();
        for (int i = 0; i < 6; i++) {
            sb.append(game.getSymbol(game.getLocation(size - i - 1)));
            sb.append("                           ");
            sb.append(game.getSymbol(game.getLocation(29 + i)));
            sb.append("\n");
        }
        for (int i = 63; i > 63 - 29; i--) {
            sb.append(game.getSymbol(game.getLocation(i)));
        }
        return sb.toString();
    }
}
