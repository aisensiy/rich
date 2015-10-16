package com.tw;

import com.tw.util.Dice;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class GameRunnerTest {
    private Game game;

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Rule
    public final TextFromStandardInputStream systemInRule = TextFromStandardInputStream.emptyStandardInputStream();
    private Dice dice;
    private GameRunner runner;

    @Before
    public void setUp() throws Exception {
        game = new Game();
        dice = mock(Dice.class);
        game.setDice(dice);
        runner = new GameRunner(game);
    }

    @Test
    public void should_show_info_to_type_init_funding() throws Exception {
        systemInRule.provideLines("2000", "23");
        runner.run();
        assertThat(systemOutRule.getLog(), startsWith("Set init funding: 1000 ~ 50000, default 10000 >"));
    }

    @Test
    public void should_set_funding() throws Exception {
        systemInRule.provideLines("2000", "23");
        runner.run();
        assertThat(game.initFunding, is(2000));
    }

    @Test
    public void should_get_default_funding_without_set() throws Exception {
        systemInRule.provideLines("", "23");
        runner.run();
        assertThat(game.initFunding, is(10000));
    }

    @Test
    public void should_set_players_to_select_players() throws Exception {
        systemInRule.provideLines("", "23");
        runner.run();
        assertThat(systemOutRule.getLog(), containsString("请选择2~4位不重复玩家，输入编号即可。(1.钱夫人; 2.阿土伯; 3.孙小美; 4.金贝贝):"));
        assertThat(game.getPlayer(1).getName(), is(Player.createPlayer(game, 2).getName()));
    }

    @Test
    public void should_show_init_map() throws Exception {
        systemInRule.provideLines("", "12");
        runner.run();
        String expected =
                "Q0000000000000H0000000000000T\n" +
                        "$                           0\n" +
                        "$                           0\n" +
                        "$                           0\n" +
                        "$                           0\n" +
                        "$                           0\n" +
                        "$                           0\n" +
                        "M0000000000000P0000000000000G";
        assertThat(systemOutRule.getLog(), containsString(expected));
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
        assertThat(runner.display(), is(expected));
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
        assertThat(runner.display(), is(expected));
    }
}