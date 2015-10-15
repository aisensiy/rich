package com.tw;

import com.tw.exception.CannotBuyToolException;
import org.junit.Before;
import org.junit.Test;

import static com.tw.Tool.*;
import static com.tw.Tool.ROADBLOCK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

public class ToolShopTest {

    private Game game;
    private Player player;
    private ToolShop toolShop;

    @Before
    public void setUp() throws Exception {
        game = mock(Game.class);
        player = Player.createPlayer(game, 1);
        toolShop = new ToolShop();
    }

    @Test
    public void should_buy_tool_in_tool_shop() throws Exception {
        player.increasePoint(50);
        int originPoint = player.getPoint();

        player.buyTool(toolShop, ROADBLOCK);
        assertThat(player.getCountOfRoadBlock(), is(1));
        assertThat(originPoint - player.getPoint(), is(ROADBLOCK.getPrice()));

        player.increasePoint(50);
        originPoint = player.getPoint();
        player.buyTool(toolShop, ROBOT);
        assertThat(player.getCountOfRobot(), is(1));
        assertThat(originPoint - player.getPoint(), is(ROBOT.getPrice()));
    }

    @Test(expected = CannotBuyToolException.class)
    public void buy_tool_should_throw_exception_when_get_more_than_10_roadblocks() throws Exception {
        player.increasePoint(11 * ROADBLOCK.getPrice());

        for (int i = 0; i < 11; i++)
            player.buyTool(toolShop, ROADBLOCK);
    }

    @Test(expected = CannotBuyToolException.class)
    public void buy_tool_should_throw_exception_when_get_more_than_10_different_tools() throws Exception {
        player.increasePoint(5 * ROADBLOCK.getPrice() + 5 * ROBOT.getPrice() + BOMB.getPrice());

        for (int i = 0; i < 5; i++)
            player.buyTool(toolShop, ROADBLOCK);

        for (int i = 0; i < 5; i++)
            player.buyTool(toolShop, ROBOT);

        player.buyTool(toolShop, BOMB);
    }

    @Test(expected = CannotBuyToolException.class)
    public void buy_tool_should_throw_exception_when_point_if_less_than_tool_price() throws Exception {
        player.increasePoint(ROADBLOCK.getPrice() - 10);
        player.buyTool(toolShop, ROADBLOCK);
    }
}
