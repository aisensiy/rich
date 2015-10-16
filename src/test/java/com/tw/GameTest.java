package com.tw;

import com.tw.exception.IllegalPlayerSettingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

public class GameTest {

    private Game game;

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Rule
    public final TextFromStandardInputStream systemInRule = TextFromStandardInputStream.emptyStandardInputStream();

    @Before
    public void setUp() throws Exception {
        game = new Game();
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
    public void should_show_info_to_type_init_funding() throws Exception {
        systemInRule.provideLines("2000", "23");
        game.run();
        assertThat(systemOutRule.getLog(), startsWith("Set init funding: 1000 ~ 50000, default 10000 >"));
    }

    @Test
    public void should_set_funding() throws Exception {
        systemInRule.provideLines("2000", "23");
        game.run();
        assertThat(game.initFunding, is(2000));
    }

    @Test
    public void should_get_default_funding_without_set() throws Exception {
        systemInRule.provideLines("", "23");
        game.run();
        assertThat(game.initFunding, is(10000));
    }

    @Test
    public void should_set_players_to_select_players() throws Exception {
        systemInRule.provideLines("", "23");
        game.run();
        assertThat(systemOutRule.getLog(), containsString("请选择2~4位不重复玩家，输入编号即可。(1.钱夫人; 2.阿土伯; 3.孙小美; 4.金贝贝):"));
        assertThat(game.getPlayer(1).getName(), is(Player.createPlayer(game, 2).getName()));
    }

    @Test
    public void should_show_map_with_player_position() throws Exception {
        game.setPlayers("12");
        game.getPlayer(2).go(2);

        String expected;
        expected =
                "Q0A00000000000H0000000000000T\n" +
                "$                           0\n" +
                "$                           0\n" +
                "$                           0\n" +
                "$                           0\n" +
                "$                           0\n" +
                "$                           0\n" +
                "M0000000000000P0000000000000G";
        assertThat(game.display(), is(expected));
        game.getPlayer(1).go(3);
        expected =
                "S0AQ0000000000H0000000000000T\n" +
                "$                           0\n" +
                "$                           0\n" +
                "$                           0\n" +
                "$                           0\n" +
                "$                           0\n" +
                "$                           0\n" +
                "M0000000000000P0000000000000G";
        assertThat(game.display(), is(expected));
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
}
