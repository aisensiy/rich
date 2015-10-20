package com.tw.location;

public enum Gift {
    MONEY(1, "奖金"), POINT(2, "点券"), GOD(3, "福神");

    private final int index;
    private final String name;

    Gift(int index, String name) {
        this.index = index;
        this.name = name;
    }
}
