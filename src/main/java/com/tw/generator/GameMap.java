package com.tw.generator;

import com.tw.Game;
import com.tw.location.Hospital;
import com.tw.location.Location;

import java.util.List;

public abstract class GameMap {
    protected List<Location> locations;
    protected Game game;

    public abstract void init();
    public abstract String display();

    public GameMap() {
        init();
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public int getHospitalIndex() {
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i).getClass() == Hospital.class) {
                return i;
            }
        }
        return -1;
    }
}
