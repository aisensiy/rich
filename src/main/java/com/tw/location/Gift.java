package com.tw.location;

import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.joining;

public enum Gift {
    MONEY(1, "奖金"), POINT(2, "点券"), GOD(3, "福神");

    private final int index;
    private final String name;

    private static final Map<Integer, Gift> map = new TreeMap<>();

    static {
        for (Gift gift : Gift.values())
            map.put(gift.index, gift);
    }

    public static Gift valueOf(int index) {
        return map.getOrDefault(index, null);
    }

    Gift(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public String toString() {
        return String.format("%s\t%d", name, index);
    }

    public static String listGifts() {
        StringBuilder sb = new StringBuilder();
        sb.append("礼品\t编号\n");
        sb.append(map.values().stream().map(Gift::toString).collect(joining("\n")));
        return sb.toString();
    }
}
