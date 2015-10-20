package com.tw.location;

import com.tw.Game;
import com.tw.Player;
import com.tw.util.Tool;
import org.junit.Test;

import static com.tw.util.Tool.BOMB;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocationTest {

    @Test
    public void should_trigger_bomb_and_send_player_to_hospital_skip_3_turn() throws Exception {
        Game game = mock(Game.class);
        when(game.getHospitalIndex()).thenReturn(10);
        Location mine = new Mine(100);
        mine.setTool(BOMB);
        Player player = Player.createPlayer(game, 1);
        mine.process(player);
        assertThat(player.getLocationIndex(), is(10));
        assertThat(player.getSkipRoll(), is(3));
        assertThat(player.getPoint(), is(0));
    }

    @Test
    public void should_show_tool_if_tool_exists() throws Exception {
        Location land = new Land(100);
        land.setTool(BOMB);
        assertThat(land.getSymbol(), is(BOMB.getSymbol()));
    }
}