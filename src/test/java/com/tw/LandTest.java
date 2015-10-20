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
        land.process(player);
        assertThat(land.getOwner(), sameInstance(player));
    }

    @Test
    public void should_decrease_money_after_buy_land_successfully() throws Exception {
        Location land = new Land(200);
        when(game.location(anyInt())).thenReturn(land);

        Player player = Player.createPlayer(game, 1);
        int originalFunding = player.getFunding();

        land.process(player);
        assertThat(originalFunding - player.getFunding(), CoreMatchers.is(200));
    }

    @Test
    public void should_buy_land_with_different_type() throws Exception {
        Location land = new Land(300);
        when(game.location(anyInt())).thenReturn(land);

        Player player = Player.createPlayer(game, 1);
        int originalFunding = player.getFunding();

        land.process(player);
        assertThat(originalFunding - player.getFunding(), CoreMatchers.is(300));
    }

    @Test(expected = NoEnoughFoundException.class)
    public void should_throw_exception_if_player_funding_is_not_enough() throws Exception {
        Land land = new Land(200);
        when(game.location(anyInt())).thenReturn(land);

        Player player = Player.createPlayer(game, 1, 20);
        land.process(player);
    }

    @Test
    public void should_not_punish_money_when_player_skip_roll() throws Exception {
        Land land = new Land(200);
        Player player1 = Player.createPlayer(game, 1);
        Player player2 = Player.createPlayer(game, 1);
        land.setOwner(player1);
        int originalFunding = player2.getFunding();
        player1.setSkipRoll(1);
        land.process(player2);
        assertThat(player2.getFunding(), is(originalFunding));
    }

    @Test
    public void should_not_punish_money_when_player_has_unpunish_roll() throws Exception {
        Land land = new Land(200);
        Player player1 = Player.createPlayer(game, 1);
        Player player2 = Player.createPlayer(game, 1);
        land.setOwner(player1);
        int originalFunding = player2.getFunding();
        player2.setUnpunishRoll(1);
        land.process(player2);
        assertThat(player2.getFunding(), is(originalFunding));
    }

    @Test
    public void should_can_punish_player_when_decrease_punish_roll_to_zero() throws Exception {
        Land land = new Land(200);
        Player player1 = Player.createPlayer(game, 1);
        Player player2 = Player.createPlayer(game, 1);
        land.setOwner(player1);
        int originalFunding = player2.getFunding();
        player2.setUnpunishRoll(1);
        player2.decreaseUnpunishRoll();
        land.process(player2);
        assertThat(player2.getFunding(), is(originalFunding - 100));
    }
}