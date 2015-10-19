package com.tw.location;

import com.tw.Game;
import com.tw.Player;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class PrisonTest {
    @Test
    public void should_skip_3_turn_in_process() throws Exception {
        Game game = mock(Game.class);
        Player player = Player.createPlayer(game, 1);
        Prison prison = new Prison();
        prison.process(player);
        assertThat(player.canRoll(), is(false));
        for (int i = 0; i < 3; i++)
            player.decreaseRoll();
        assertThat(player.canRoll(), is(true));
    }
}