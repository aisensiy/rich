package com.tw;

import com.tw.exception.NoEnoughFoundException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerTest {

    private Game game;

    @Before
    public void setUp() throws Exception {
        game = mock(Game.class);
    }

    @Test
    public void should_get_location_0_for_all_users() throws Exception {
        assertThat(Player.createPlayer(game, 1).getLocationIndex(), is(0));
        assertThat(Player.createPlayer(game, 2).getLocationIndex(), is(0));
    }

    @Test
    public void should_get_new_location_after_roll() throws Exception {
        Player player = Player.createPlayer(game, 1);
        player.go(3);
        assertThat(player.getLocationIndex(), is(3));

        player.go(3);
        assertThat(player.getLocationIndex(), is(6));
    }

    @Test
    public void should_go_back_to_start_point_after_location_out_of_the_map() throws Exception {
        Player player = Player.createPlayer(game, 1);
        player.setCurrentLocation(69);
        player.go(2);
        assertThat(player.getLocationIndex(), is(1));
    }

    @Test
    public void should_get_type_of_location() throws Exception {
        when(game.location(anyInt())).thenReturn(new Location("land"));
        Player player = Player.createPlayer(game, 1);
        assertThat(player.getCurrentLocation().getType(), is("land"));

        when(game.location(anyInt())).thenReturn(new Location("build"));
        assertThat(player.getCurrentLocation().getType(), is("build"));
    }

    @Test
    public void can_buy_land_if_current_location_is_land_and_empty() throws Exception {
        Location land = new Location("land");
        when(game.location(anyInt())).thenReturn(land);
        Player player = Player.createPlayer(game, 1);
        player.buyEmptyLand();
        assertThat(land.getOwner(), sameInstance(player));
    }

    @Test(expected = CannotBuyLocationException.class)
    public void throw_exception_when_buy_land_if_current_location_if_not_land() throws Exception {
        when(game.location(anyInt())).thenReturn(new Location("start"));
        Player.createPlayer(game, 1).buyEmptyLand();
    }

    @Test(expected = CannotBuyLocationException.class)
    public void throw_exception_when_buy_land_if_current_location_if_not_empty() throws Exception {
        Player player1 = Player.createPlayer(game, 1);
        Player player2 = Player.createPlayer(game, 2);

        Location landOwnByOthers = new Location("land");

        landOwnByOthers.setOwner(player2);
        when(game.location(anyInt())).thenReturn(landOwnByOthers);

        player1.buyEmptyLand();
    }

    @Test
    public void should_decrease_money_after_buy_land_successfully() throws Exception {
        Location land = new Location("land");
        when(game.location(anyInt())).thenReturn(land);

        Player player = Player.createPlayer(game, 1);
        int originalFunding = player.getFunding();

        player.buyEmptyLand();
        assertThat(originalFunding - player.getFunding(), is(200));
    }

    @Test(expected = NoEnoughFoundException.class)
    public void should_throw_exception_if_player_funding_is_not_enough() throws Exception {
        Location land = new Location("land");
        when(game.location(anyInt())).thenReturn(land);

        Player player = Player.createPlayer(game, 1, 20);
        player.buyEmptyLand();
    }

    
}