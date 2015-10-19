package com.tw.util;

import com.tw.Player;
import com.tw.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tw.Tool.BOMB;
import static com.tw.Tool.ROADBLOCK;
import static com.tw.Tool.ROBOT;

public class ToolBox {
    private List<Tool> tools = new ArrayList<>();
    private Player player;

    public ToolBox(Player player) {
        this.player = player;
    }

    public int getToolCount() {
        return tools.size();
    }

    public int getCountOfBomb() {
        return (int) tools.stream().filter(t -> t == Tool.BOMB).count();
    }

    public int getCountOfRoadBlock() {
        return (int) tools.stream().filter(t -> t == Tool.ROADBLOCK).count();
    }

    public int getCountOfRobot() {
        return (int) tools.stream().filter(t -> t == Tool.ROBOT).count();
    }

    public void addTool(Tool tool) {
        tools.add(tool);
    }

    public void remoteTool(Tool tool) {
        tools.remove(tool);
    }

    @Override
    public String toString() {
        return String.format("道具: 路障%d个；炸弹%d个；机器娃娃%d个", getCountOfRoadBlock(), getCountOfBomb(), getCountOfRobot());
    }
}
