package com.tw;

public class Land extends Location {
    private int price = 200;
    private int level = 0;


    public int getPrice() {
        return price;
    }

    public int getLevel() {
        return level;
    }

    public void upgradeLevel() {
        level++;
    }
}
