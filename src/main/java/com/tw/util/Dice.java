package com.tw.util;

public class Dice {
    public int getInt() {
        return new java.util.Random().nextInt(6) + 1;
    }
}
