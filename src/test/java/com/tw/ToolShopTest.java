package com.tw;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

public class ToolShopTest {

    private Game game;

    @Before
    public void setUp() throws Exception {
        game = mock(Game.class);
    }

    @Test
    public void should_buy_tool_in_tool_shop() throws Exception {
        Player player = Player.createPlayer(game, 1);
        player.increasePoint(50);
        int originPoint = player.getPoint();
        ToolShop toolShop = new ToolShop();

        player.buyTool(toolShop, Tool.ROADBLOCK);
        assertThat(player.getCountOfRoadBlock(), is(1));
        assertThat(originPoint - player.getPoint(), is(Tool.ROADBLOCK.getPrice()));

        player.increasePoint(50);
        originPoint = player.getPoint();
        player.buyTool(toolShop, Tool.ROBOT);
        assertThat(player.getCountOfRobot(), is(1));
        assertThat(originPoint - player.getPoint(), is(Tool.ROBOT.getPrice()));
    }
}
