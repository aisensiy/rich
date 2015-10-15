package com.tw;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

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

//    @Test
//    public void should_get_type_of_location() throws Exception {
//        Player player = Player.createPlayer(game, 1);
//        player.go(2);
//        assertThat(player.getCurrentLocation().getType(), is("land"));
//    }
}