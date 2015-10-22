package com.tw.location;

import com.tw.Game;
import com.tw.Player;
import com.tw.exception.CannotBuyToolException;
import com.tw.location.ToolShop;
import org.junit.Before;
import org.junit.Test;

import static com.tw.util.Tool.*;
import static com.tw.util.Tool.ROADBLOCK;
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

        player.buyTool(ROADBLOCK);
        assertThat(player.getCountOf(ROADBLOCK), is(1));
        assertThat(originPoint - player.getPoint(), is(ROADBLOCK.getPrice()));

        player.increasePoint(50);
        originPoint = player.getPoint();
        player.buyTool(ROBOT);
        assertThat(player.getCountOf(ROBOT), is(1));
        assertThat(originPoint - player.getPoint(), is(ROBOT.getPrice()));
    }

    @Test(expected = CannotBuyToolException.class)
    public void buy_tool_should_throw_exception_when_get_more_than_10_roadblocks() throws Exception {
        player.increasePoint(11 * ROADBLOCK.getPrice());

        for (int i = 0; i < 11; i++)
            player.buyTool(ROADBLOCK);
    }

    @Test(expected = CannotBuyToolException.class)
    public void buy_tool_should_throw_exception_when_get_more_than_10_different_tools() throws Exception {
        player.increasePoint(5 * ROADBLOCK.getPrice() + 5 * ROBOT.getPrice() + BOMB.getPrice());

        for (int i = 0; i < 5; i++)
            player.buyTool(ROADBLOCK);

        for (int i = 0; i < 5; i++)
            player.buyTool(ROBOT);

        player.buyTool(BOMB);
    }

    @Test(expected = CannotBuyToolException.class)
    public void buy_tool_should_throw_exception_when_point_if_less_than_tool_price() throws Exception {
        player.increasePoint(ROADBLOCK.getPrice() - 10);
        player.buyTool(ROADBLOCK);
    }
}
