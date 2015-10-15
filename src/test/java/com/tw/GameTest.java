package com.tw;

import com.tw.exception.IllegalPlayerSettingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

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
        systemInRule.provideText("2000\n");
        game.run();
        assertThat(systemOutRule.getLog(), startsWith("Set init funding: 1000 ~ 50000, default 10000 >"));
    }

    @Test
    public void should_set_funding() throws Exception {
        systemInRule.provideText("2000\n");
        game.run();
        assertThat(game.initFunding, is(2000));
    }

    @Test
    public void should_get_default_funding_without_set() throws Exception {
        systemInRule.provideLines("");
        game.run();
        assertThat(game.initFunding, is(10000));
    }
}
