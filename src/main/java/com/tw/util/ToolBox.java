package com.tw.util;

import com.tw.Player;

import java.util.ArrayList;
import java.util.List;

import static com.tw.util.Tool.BOMB;
import static com.tw.util.Tool.ROADBLOCK;
import static com.tw.util.Tool.ROBOT;

public class ToolBox {
    private List<Tool> tools = new ArrayList<>();
    private Player player;

    public ToolBox(Player player) {
        this.player = player;
    }

    public int getToolCount() {
        return tools.size();
    }

    public void addTool(Tool tool) {
        tools.add(tool);
    }

    public void remoteTool(Tool tool) {
        tools.remove(tool);
    }

    @Override
    public String toString() {
        return String.format("道具: 路障%d个；炸弹%d个；机器娃娃%d个", getCountOf(ROADBLOCK), getCountOf(BOMB), getCountOf(ROBOT));
    }

    public int getCountOf(Tool tool) {
        return (int) tools.stream().filter(t -> t == tool).count();
    }
}
