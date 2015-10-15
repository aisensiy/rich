package com.tw;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PlayerTest {

    @Test
    public void should_get_location_0_for_all_users() throws Exception {
        assertThat(Player.createPlayer(1).getLocation(), is(0));
        assertThat(Player.createPlayer(2).getLocation(), is(0));
    }

    @Test
    public void should_get_new_location_after_roll() throws Exception {
        Player player = Player.createPlayer(1);
        player.go(3);
        assertThat(player.getLocation(), is(3));

        player.go(3);
        assertThat(player.getLocation(), is(6));
    }

    @Test
    public void should_go_back_to_start_point_after_location_out_of_the_map() throws Exception {
        Player player = Player.createPlayer(1);
        player.setCurrentLocation(69);
        player.go(2);
        assertThat(player.getLocation(), is(1));
    }
}