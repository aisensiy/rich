package com.tw;

import com.tw.exception.IllegalPlayerSettingException;
import com.tw.util.Dice;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {

    private Game game;

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Rule
    public final TextFromStandardInputStream systemInRule = TextFromStandardInputStream.emptyStandardInputStream();
    private Dice dice;

    @Before
    public void setUp() throws Exception {
        game = new Game();
        dice = mock(Dice.class);
        game.setDice(dice);
    }

    @Test
    public void should_init_funding_for_users() throws Exception {
        assertThat(game.initFunding, is(Game.DEFAULT_FUNDING));

        game.setInitFunding(1000);
        game.setPlayers("12");
        assertThat(game.getPlayer(1).getFunding(), is(1000));
    }

    @Test
    public void should_set_roles_for_players() throws Exception {
        game.setPlayers("12");
        assertThat(game.getPlayer(1).getName(), is("钱夫人"));
    }

    @Test(expected = IllegalPlayerSettingException.class)
    public void should_throw_exception_with_player_input_length_less_than_2() throws Exception {
        game.setPlayers("1");
    }

    @Test(expected = IllegalPlayerSettingException.class)
    public void should_throw_exception_with_invalid_player_input_length_more_than_4() throws Exception {
        game.setPlayers("12345");
    }

    @Test(expected = IllegalPlayerSettingException.class)
    public void should_throw_exception_with_player_number_not_in_1_to_4() throws Exception {
        Game game = new Game();
        game.setPlayers("1235");
    }

    @Test(expected = IllegalPlayerSettingException.class)
    public void should_throw_exception_with_duplicate_players() throws Exception {
        Game game = new Game();
        game.setPlayers("1132");
    }

    @Test
    public void should_current_player_change_to_another_player_after_roll_with_2_players() throws Exception {
        game.setPlayers("12");
        Player anotherPlayer = game.getPlayer(2);
        game.roll();
        assertThat(game.getCurrentPlayer(), is(anotherPlayer));
    }

    @Test
    public void should_current_player_change_to_another_player_after_roll_with_3_players() throws Exception {
        game.setPlayers("312");
        Player player1 = game.getCurrentPlayer();
        Player player2 = game.getPlayer(2);
        Player player3 = game.getPlayer(3);

        game.roll();
        assertThat(game.getCurrentPlayer(), is(player2));
        game.roll();
        assertThat(game.getCurrentPlayer(), is(player3));
        game.roll();
        assertThat(game.getCurrentPlayer(), is(player1));
    }

    @Test
    public void should_update_current_player_location_after_roll() throws Exception {
        when(dice.getInt()).thenReturn(5);
        game.setPlayers("12");
        Player player1 = game.getCurrentPlayer();
        Player player2 = game.getPlayer(2);

        int player1OriginalLocation = game.getCurrentPlayer().getLocationIndex();
        game.roll();
        assertThat(player1.getLocationIndex() - player1OriginalLocation, is(5));

        int player2OriginalLocation = game.getCurrentPlayer().getLocationIndex();
        game.roll();
        assertThat(player2.getLocationIndex() - player2OriginalLocation, is(5));
    }
}
