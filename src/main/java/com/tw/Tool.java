package com.tw;

import java.util.HashMap;
import java.util.Map;

public enum Tool {
    ROADBLOCK(1, 50), ROBOT(2, 30), BOMB(3, 50);

    private static final Map<Integer, Tool> map = new HashMap<>();

    static {
        for (Tool tool : Tool.values())
            map.put(tool.index, tool);
    }

    private final int price;
    private final int index;

    private Tool(int index, int price) {
        this.index = index;
        this.price = price;
    }

    public static Tool valueOf(int index) {
        return map.getOrDefault(index, null);
    }

    public int getPrice() {
        return price;
    }
}
