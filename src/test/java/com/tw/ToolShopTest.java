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
        Player player = Player.createPlayer(game, 1);
        player.increasePoint(11 * ROADBLOCK.getPrice());
        ToolShop toolShop = new ToolShop();

        for (int i = 0; i < 11; i++)
            player.buyTool(toolShop, ROADBLOCK);
    }

    @Test(expected = CannotBuyToolException.class)
    public void buy_tool_should_throw_exception_when_get_more_than_10_different_tools() throws Exception {
        Player player = Player.createPlayer(game, 1);
        player.increasePoint(5 * ROADBLOCK.getPrice() + 5 * ROBOT.getPrice() + BOMB.getPrice());
        ToolShop toolShop = new ToolShop();

        for (int i = 0; i < 5; i++)
            player.buyTool(toolShop, ROADBLOCK);

        for (int i = 0; i < 5; i++)
            player.buyTool(toolShop, ROBOT);

        player.buyTool(toolShop, BOMB);
    }
}
