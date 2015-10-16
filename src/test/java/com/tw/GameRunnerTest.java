package com.tw;

import com.tw.location.Land;
import com.tw.map.HospitalMap;
import com.tw.map.LandMap;
import com.tw.map.MineMap;
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
        systemInRule.provideLines("2000");
        runner.setInitFunding();
        assertThat(systemOutRule.getLog(), startsWith("Set init funding: 1000 ~ 50000, default 10000 >"));
    }

    @Test
    public void should_set_funding() throws Exception {
        systemInRule.provideLines("2000");
        runner.setInitFunding();
        assertThat(game.initFunding, is(2000));
    }

    @Test
    public void should_get_default_funding_without_set() throws Exception {
        systemInRule.provideLines("");
        runner.setInitFunding();
        assertThat(game.initFunding, is(10000));
    }

    @Test
    public void should_set_players_to_select_players() throws Exception {
        systemInRule.provideLines("23");
        runner.setPlayers();
        assertThat(systemOutRule.getLog(), containsString("请选择2~4位不重复玩家，输入编号即可。(1.钱夫人; 2.阿土伯; 3.孙小美; 4.金贝贝):"));
        assertThat(game.getPlayer(1).getName(), is(Player.createPlayer(game, 2).getName()));
    }

    @Test
    public void should_show_map_with_player_position() throws Exception {
        game.setPlayers("12");
        String expected =
                "Q0000000000000H0000000000000T\n" +
                        "$                           0\n" +
                        "$                           0\n" +
                        "$                           0\n" +
                        "$                           0\n" +
                        "$                           0\n" +
                        "$                           0\n" +
                        "M0000000000000P0000000000000G";
        assertThat(runner.display(), containsString(expected));

        game.getPlayer(2).go(2);

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

    @Test
    public void should_show_current_player_name_and_symbol() throws Exception {
        game.setPlayers("123");
        runner.turn();
        assertThat(systemOutRule.getLog(), containsString("钱夫人(Q)>"));

        game.setPlayers("32");
        runner.turn();
        assertThat(systemOutRule.getLog(), containsString("孙小美(S)>"));
    }

    @Test
    public void should_receive_roll_command_and_update_location() throws Exception {
        game = new Game(new HospitalMap());
        game.setDice(dice);

        game.setPlayers("12");
        runner.setGame(game);

        when(dice.getInt()).thenReturn(1);
        runner.turn();
        assertThat(game.getPlayer(1).getLocationIndex(), is(1));
        assertThat(game.getCurrentPlayer(), is(game.getPlayer(2)));

        when(dice.getInt()).thenReturn(2);
        runner.turn();
        assertThat(game.getPlayer(2).getLocationIndex(), is(2));
        assertThat(game.getCurrentPlayer(), is(game.getPlayer(1)));
    }

    @Test
    public void should_run_infinit_without_quit_command() throws Exception {
        when(dice.getInt()).thenReturn(5);
        systemInRule.provideLines("", "12", "roll", "n", "roll", "n", "roll", "n", "quit");
        runner.run();
        assertThat(game.getCurrentPlayer(), is(game.getPlayer(2)));
        assertThat(game.getPlayer(1).getLocationIndex(), is(10));
        assertThat(game.getPlayer(2).getLocationIndex(), is(5));
    }

    @Test
    public void should_show_player_info_with_command_query() throws Exception {
        systemInRule.provideLines("", "12", "query", "quit");
        runner.run();
        assertThat(systemOutRule.getLog(), containsString(game.getCurrentPlayer().getInfo()));
    }

    @Test
    public void should_show_help_with_command_help() throws Exception {
        systemInRule.provideLines("", "12", "help", "quit");
        runner.run();
        assertThat(systemOutRule.getLog(), containsString(runner.help()));
    }

    @Test
    public void should_ask_buy_land_when_arrive_at_empty_land() throws Exception {
        when(dice.getInt()).thenReturn(3);
        systemInRule.provideLines("", "12", "roll", "Y", "roll", "query", "quit");
        runner.run();
        assertThat(systemOutRule.getLog(), containsString("是否购买该处空地，200 元（Y/N）? "));
        assertThat(systemOutRule.getLog(), containsString("空地1处"));
    }

    @Test
    public void should_upgrade_land_when_arrive_at_own_land() throws Exception {
        game = gameWithOneLand();
        when(dice.getInt()).thenReturn(1);
        systemInRule.provideLines("", "12", "roll", "y", "roll", "roll", "y", "quit");
        runner.run();
        Land land = (Land) game.getLocation(0);
        assertThat(systemOutRule.getLog(), containsString("是否升级该处地产，200 元（Y/N）?"));
        assertThat(land.getLevel(), is(1));
    }

    @Test
    public void should_show_error_message_when_upgrade_land_with_level_3() throws Exception {
        game = gameWithOneLand();
        when(dice.getInt()).thenReturn(1);
        systemInRule.provideLines("", "12", "roll", "y", "roll", "roll", "y", "roll", "roll", "y", "roll", "roll", "y", "roll", "roll", "y", "quit");
        runner.run();
        assertThat(systemOutRule.getLog(), containsString("can not upgrade land with highest level"));
    }

    @Test
    public void should_get_point_when_arrive_at_mine() throws Exception {
        game = gameWithOneMine();
        when(dice.getInt()).thenReturn(1);
        systemInRule.provideLines("", "12", "roll", "roll", "query", "quit");
        runner.run();
        assertThat(systemOutRule.getLog(), containsString("点数: 100"));
        assertThat(systemOutRule.getLog(), containsString("在矿点获取点数100"));
    }

    private Game gameWithOneMine() {
        Game game = new Game(new MineMap());
        game.setDice(dice);
        runner.setGame(game);
        return game;
    }

    private Game gameWithOneLand() {
        Game game = new Game(new LandMap());
        game.setDice(dice);
        runner.setGame(game);
        return game;
    }
}