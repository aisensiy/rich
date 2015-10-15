package com.tw;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MineTest {

    private Game game;

    @Before
    public void setUp() throws Exception {
        game = new Game();
    }

    @Test
    public void should_add_point_when_arrive_at_mine() throws Exception {
        Player player = Player.createPlayer(game, 1);
        int minePoint = 20;
        Location mine = new Mine(minePoint);
        int originalPoint = player.getPoint();
        mine.process(player);
        assertThat(player.getPoint() - originalPoint, is(minePoint));
    }


}
