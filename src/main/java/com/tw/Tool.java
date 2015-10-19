package com.tw;

import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.joining;

public enum Tool {
    ROADBLOCK(1, "路障", 50, "#"), ROBOT(2, "机器娃娃", 30, ""), BOMB(3, "炸弹", 50, "@");

    private static final Map<Integer, Tool> map = new TreeMap<>();

    static {
        for (Tool tool : Tool.values())
            map.put(tool.index, tool);
    }

    private final int price;
    private final int index;
    private final String name;
    private final String symbol;

    private Tool(int index, String name, int price, String symbol) {
        this.index = index;
        this.name = name;
        this.price = price;
        this.symbol = symbol;
    }

    public static Tool valueOf(int index) {
        return map.getOrDefault(index, null);
    }

    public int getPrice() {
        return price;
    }

    public String toString() {
        return String.format("%s\t%d\t%d\t%s", name, index, price, symbol);
    }

    public static String listTools() {
        StringBuilder sb = new StringBuilder();
        sb.append("道具\t编号\t点数\t显示方式");
        sb.append(map.values().stream().map(Tool::toString).collect(joining("\n")));
        return sb.toString();
    }
}
