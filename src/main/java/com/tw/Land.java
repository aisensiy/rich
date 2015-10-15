package com.tw;

public class Land extends Location {
    private int price;
    private int level;

    public Land(int price) {
        super();
        this.price = price;
        level = 0;
    }

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

    public boolean isHighestLevel() {
        return level == 3;
    }
}
