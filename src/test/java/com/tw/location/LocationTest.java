package com.tw.location;

import com.tw.Game;
import com.tw.Player;
import com.tw.util.Dice;
import org.junit.Before;
import org.junit.Test;

import static com.tw.Tool.BOMB;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocationTest {

    @Test
    public void should_trigger_bomb_and_send_player_to_hospital_skip_3_turn() throws Exception {
        Game game = mock(Game.class);
        when(game.getHospitalIndex()).thenReturn(10);
        Location land = new Land(100);
        land.setTool(BOMB);
        Player player = Player.createPlayer(game, 1);
        land.process(player);
        assertThat(player.getLocationIndex(), is(10));
        assertThat(player.getSkipRoll(), is(3));
    }
}