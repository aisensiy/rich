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

    @Override
    public String getName() {
        switch (level) {
            case 1:
                return "茅屋";
            case 2:
                return "洋房";
            case 3:
                return "摩天楼";
            default:
                return "空地";
        }
    }
}
