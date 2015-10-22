package com.tw.location;

import com.tw.Game;
import com.tw.Player;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

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
    public void should_not_punish_money_when_player_skip_roll() throws Exception {
        Land land = new Land(200);
        Player player1 = Player.createPlayer(game, 1);
        Player player2 = Player.createPlayer(game, 1);
        land.setOwner(player1);
        int originalFunding = player2.getFunding();
        player1.setSkipTurn(1);
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
        player2.setUnpunishTurn(1);
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
        player2.setUnpunishTurn(1);
        player2.decreaseUnpunishRoll();
        land.process(player2);
        assertThat(player2.getFunding(), is(originalFunding - 100));
    }
}