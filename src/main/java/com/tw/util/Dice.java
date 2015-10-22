package com.tw.util;

public class Dice {
    public static Dice dice;
    public int getInt() {
        return new java.util.Random().nextInt(6) + 1;
    }

    public static int roll() {
        if (dice == null) {
            dice = new Dice();
        }
        return dice.getInt();
    }
}
