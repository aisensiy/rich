package com.tw.util;

import com.tw.Player;
import com.tw.Tool;

import java.util.HashMap;
import java.util.Map;

import static com.tw.Tool.BOMB;
import static com.tw.Tool.ROADBLOCK;
import static com.tw.Tool.ROBOT;

public class ToolBox {
    private Map<Tool, Integer> toolMap = new HashMap<>();
    private Player player;

    public ToolBox(Player player) {
        this.player = player;
    }

    public int getToolCount() {
        return getCountOfRoadBlock() + getCountOfRobot() + getCountOfBomb();
    }

    public int getCountOfBomb() {
        return toolMap.getOrDefault(BOMB, 0);
    }

    public int getCountOfRoadBlock() {
        return toolMap.getOrDefault(ROADBLOCK, 0);
    }

    public void addTool(Tool tool) {
        toolMap.put(tool, toolMap.getOrDefault(tool, 0) + 1);
    }

    public int getCountOfRobot() {
        return toolMap.getOrDefault(ROBOT, 0);
    }

    @Override
    public String toString() {
        return String.format("道具: 路障%d个；炸弹%d个；机器娃娃%d个", getCountOfRoadBlock(), getCountOfBomb(), getCountOfRobot());
    }
}
