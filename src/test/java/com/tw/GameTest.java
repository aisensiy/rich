package com.tw;

import com.tw.exception.IllegalPlayerSettingException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GameTest {

    private Game game;

    @Before
    public void setUp() throws Exception {
        game = new Game();
    }

    @Test
    public void should_init_funding_for_users() {
        assertThat(game.initFunding, is(Game.DEFAULT_FUNDING));

        game.setInitFunding(1000);
        assertThat(game.initFunding, is(1000));
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

//    @Test(expected = IllegalPlayerSettingException.class)
//    public void should_throw_exception_with_player_number_not_in_1_to_4() throws Exception {
//        Game game = new Game();
//        game.setPlayers("1235");
//    }
}
