package com.tw;

import com.tw.exception.NoEnoughFoundException;
import com.tw.location.Land;
import com.tw.location.Location;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LandTest {
    private Game game;

    @Before
    public void setUp() throws Exception {
        game = mock(Game.class);
    }

    @Test
    public void should_get_different_name_of_land_with_different_level() throws Exception {
        Land land = new Land(200);
        assertThat(land.getName(), is("空地"));

        land.upgradeLevel();
        assertThat(land.getName(), is("茅屋"));

        land.upgradeLevel();
        assertThat(land.getName(), is("洋房"));

        land.upgradeLevel();
        assertThat(land.getName(), is("摩天楼"));
    }

    @Test
    public void can_buy_land_if_current_location_is_land_and_empty() throws Exception {
        Location land = new Land(200);
        when(game.location(anyInt())).thenReturn(land);
        Player player = Player.createPlayer(game, 1);
        player.buyEmptyLand();
        assertThat(land.getOwner(), sameInstance(player));
    }

    @Test
    public void should_decrease_money_after_buy_land_successfully() throws Exception {
        Location land = new Land(200);
        when(game.location(anyInt())).thenReturn(land);

        Player player = Player.createPlayer(game, 1);
        int originalFunding = player.getFunding();

        player.buyEmptyLand();
        assertThat(originalFunding - player.getFunding(), CoreMatchers.is(200));
    }

    @Test
    public void should_buy_land_with_different_type() throws Exception {
        Location land = new Land(300);
        when(game.location(anyInt())).thenReturn(land);

        Player player = Player.createPlayer(game, 1);
        int originalFunding = player.getFunding();

        player.buyEmptyLand();
        assertThat(originalFunding - player.getFunding(), CoreMatchers.is(300));
    }

    @Test(expected = NoEnoughFoundException.class)
    public void should_throw_exception_if_player_funding_is_not_enough() throws Exception {
        Land land = new Land(200);
        when(game.location(anyInt())).thenReturn(land);

        Player player = Player.createPlayer(game, 1, 20);
        player.buyEmptyLand();
    }
}