package com.tw;

import com.tw.exception.IllegalPlayerSettingException;
import com.tw.exception.RichGameException;
import com.tw.util.Dice;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.junit.rules.ExpectedException;

import static com.tw.Tool.BOMB;
import static com.tw.Tool.ROADBLOCK;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {

    private Game game;

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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
        game.setCurrentPlayerToNext();
        assertThat(game.getCurrentPlayer(), is(anotherPlayer));
    }

    @Test
    public void should_current_player_change_to_another_player_after_roll_with_3_players() throws Exception {
        game.setPlayers("312");
        Player player1 = game.getCurrentPlayer();
        Player player2 = game.getPlayer(2);
        Player player3 = game.getPlayer(3);

        game.roll();
        game.setCurrentPlayerToNext();
        assertThat(game.getCurrentPlayer(), is(player2));
        game.roll();
        game.setCurrentPlayerToNext();
        assertThat(game.getCurrentPlayer(), is(player3));
        game.roll();
        game.setCurrentPlayerToNext();
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
        game.setCurrentPlayerToNext();
        assertThat(player1.getLocationIndex() - player1OriginalLocation, is(5));

        int player2OriginalLocation = game.getCurrentPlayer().getLocationIndex();
        game.roll();
        game.setCurrentPlayerToNext();
        assertThat(player2.getLocationIndex() - player2OriginalLocation, is(5));
    }

    @Test
    public void should_set_current_player_to_next() throws Exception {
        game.setPlayers("1234");
        game.setCurrentPlayerToNext();
        assertThat(game.getCurrentPlayer(), is(game.getPlayer(2)));
        game.setCurrentPlayerToNext();
        assertThat(game.getCurrentPlayer(), is(game.getPlayer(3)));
        game.setCurrentPlayerToNext();
        assertThat(game.getCurrentPlayer(), is(game.getPlayer(4)));
        game.setCurrentPlayerToNext();
        assertThat(game.getCurrentPlayer(), is(game.getPlayer(1)));
    }

    @Test
    public void should_show_the_nearest_player_to_roll_with_overlap() throws Exception {
        game.setPlayers("123");
        assertThat(game.getSymbol(game.getLocation(0)), is("Q"));
        when(dice.getInt()).thenReturn(1);
        game.roll();
        game.setCurrentPlayerToNext();
        when(dice.getInt()).thenReturn(3);
        game.roll();
        game.setCurrentPlayerToNext();
        when(dice.getInt()).thenReturn(2);
        game.roll();
        game.setCurrentPlayerToNext();
        when(dice.getInt()).thenReturn(2);
        game.roll();
        game.setCurrentPlayerToNext();
        assertThat(game.getSymbol(game.getLocation(3)), is("A"));
    }

    @Test
    public void should_get_new_location_after_roll() throws Exception {
        Player player = Player.createPlayer(game, 1);
        assertThat(game.forward(player, 3), is(3));

        player.setCurrentLocation(game.getMapSize() - 1);
        game.forward(player, 3);
        assertThat(game.forward(player, 3), is(2));
    }

    @Test
    public void should_remove_player() throws Exception {
        game.setPlayers("12");
        Player player = game.getPlayer(1);
        game.removePlayer(player);
        assertThat(game.isOver(), is(true));
    }

    @Test
    public void should_remove_player_and_get_next_player() throws Exception {
        game.setPlayers("123");
        Player player = game.getPlayer(1);
        game.removePlayer(player);
        game.setCurrentPlayerToNext();
        assertThat(game.getCurrentPlayer(), is(game.getPlayer(1)));
    }

    @Test
    public void should_skip_player_who_cannot_roll() throws Exception {
        game.setPlayers("123");
        game.getPlayer(2).setSkipRoll(1);
        game.setCurrentPlayerToNext();
        assertThat(game.getCurrentPlayer(), is(game.getPlayer(3)));
        game.setCurrentPlayerToNext();
        game.setCurrentPlayerToNext();
        assertThat(game.getCurrentPlayer(), is(game.getPlayer(2)));
    }

    @Test
    public void should_set_block_by_current_player() throws Exception {
        game.setPlayers("12");
        Player player = game.getPlayer(1);
        player.addTool(ROADBLOCK);
        game.setTool(ROADBLOCK, 5);
        assertThat(game.getRelativeLocationWithCurrent(5).getTool(), is(ROADBLOCK));
        assertThat(player.getCountOf(ROADBLOCK), is(0));
    }

    @Test
    public void should_set_bomb_by_current_player() throws Exception {
        game.setPlayers("12");
        Player player = game.getPlayer(1);
        player.addTool(BOMB);
        game.setTool(BOMB, 5);
        assertThat(game.getRelativeLocationWithCurrent(5).getTool(), is(BOMB));
        assertThat(player.getCountOf(BOMB), is(0));
    }

    @Test
    public void should_throw_exception_when_set_road_block_which_is_not_enough() throws Exception {
        expectedException.expect(RichGameException.class);
        expectedException.expectMessage("no such tool");
        game.setPlayers("12");
        Player player = game.getPlayer(1);
        game.setTool(ROADBLOCK, 5);
    }

    @Test
    public void should_throw_exception_when_set_bomb_which_is_not_enough() throws Exception {
        expectedException.expect(RichGameException.class);
        expectedException.expectMessage("no such tool");
        game.setPlayers("12");
        game.setTool(BOMB, 5);
    }

    @Test
    public void throw_exception_when_there_is_tool_on_location() throws Exception {
        expectedException.expect(RichGameException.class);
        expectedException.expectMessage("there is already a tool on the location");
        game.setPlayers("12");
        Player player = game.getPlayer(1);
        player.addTool(ROADBLOCK);
        player.addTool(ROADBLOCK);
        game.setTool(ROADBLOCK, 5);
        game.setTool(ROADBLOCK, 5);
    }

    @Test
    public void set_tool_should_throw_exception_when_there_is_player_on_location() throws Exception {
        expectedException.expect(RichGameException.class);
        expectedException.expectMessage("there is a player on the location");
        game.setPlayers("12");
        Player player = game.getPlayer(1);
        player.addTool(ROADBLOCK);
        game.getPlayer(2).go(5);
        game.setTool(ROADBLOCK, 5);
    }

    @Test
    public void set_tool_should_throw_exception_when_index_is_out_of_range() throws Exception {
        expectedException.expectMessage("range should between -10 and 10");
        game.setPlayers("12");
        Player player = game.getPlayer(1);
        player.addTool(ROADBLOCK);
        game.setTool(ROADBLOCK, 11);
    }
}
