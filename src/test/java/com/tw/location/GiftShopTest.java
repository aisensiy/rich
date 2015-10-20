package com.tw.location;

import com.tw.Game;
import com.tw.Player;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class GiftShopTest {
    private Game game;
    private Player player;

    @Before
    public void setUp() throws Exception {
        game = mock(Game.class);
        player = Player.createPlayer(game, 1);
    }

    @Test
    public void should_gain_money_when_select_MONEY() throws Exception {
        int originalFunding = player.getFunding();
        GiftShop giftShop = new GiftShop();
        giftShop.get(player, Gift.MONEY);
        assertThat(player.getFunding() - originalFunding, is(2000));
    }

    @Test
    public void should_gain_point_when_select_POINT() throws Exception {
        int originalPoint = player.getPoint();
        GiftShop giftShop = new GiftShop();
        giftShop.get(player, Gift.POINT);
        assertThat(player.getPoint() - originalPoint, is(200));
    }
}