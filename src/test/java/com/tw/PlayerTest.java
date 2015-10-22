package com.tw;

import com.tw.exception.CannotAccessLandException;
import com.tw.exception.NoEnoughFoundException;
import com.tw.location.Land;
import com.tw.location.Location;
import com.tw.util.Dice;
import com.tw.util.Tool;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerTest {

    private Game game;
    private Player player;
    private Dice dice;

    @Before
    public void setUp() throws Exception {
        game = mock(Game.class);
        when(game.getMapSize()).thenReturn(70);
        player = Player.createPlayer(game, 1);
        dice = mock(Dice.class);
        Dice.dice = dice;
    }

    @Test
    public void should_get_location_0_for_all_users() throws Exception {
        assertThat(Player.createPlayer(game, 1).getLocationIndex(), is(0));
        assertThat(Player.createPlayer(game, 2).getLocationIndex(), is(0));
    }

    @Test
    public void should_get_type_of_land() throws Exception {
        when(game.getLocation(anyInt())).thenReturn(new Land(200));
        Player player = Player.createPlayer(game, 1);
        assertThat(player.getCurrentLocation().isLand(), is(true));
    }

    @Test
    public void should_upgrade_level_by_1_after_upgrade() throws Exception {
        Player player = Player.createPlayer(game, 1);
        Land land = createLandWithOwner(player);
        when(game.getLocation(anyInt())).thenReturn(land);
        assertThat(land.getLevel(), is(0));

        player.upgradeLand(land);
        assertThat(land.getLevel(), is(1));
    }

    @Test
    public void can_buy_land_if_current_location_is_land_and_empty() throws Exception {
        Land land = new Land(200);
        player.buyLand(land);
        assertThat(land.getOwner(), sameInstance(player));
    }

    @Test
    public void should_decrease_money_after_buy_land_successfully() throws Exception {
        Land land = new Land(200);

        int originalFunding = player.getFunding();

        player.buyLand(land);
        assertThat(originalFunding - player.getFunding(), CoreMatchers.is(200));
    }

    @Test
    public void should_buy_land_with_different_type() throws Exception {
        Land land = new Land(300);

        int originalFunding = player.getFunding();

        player.buyLand(land);
        assertThat(originalFunding - player.getFunding(), CoreMatchers.is(300));
    }

    @Test(expected = NoEnoughFoundException.class)
    public void should_throw_exception_if_player_funding_is_not_enough() throws Exception {
        player.buyLand(new Land(1000000));
    }


    @Test
    public void upgradeLand_should_decrease_same_money_as_buy_the_land_after_upgrade() throws Exception {
        Land land = createLandWithOwner(player);
        int originalFunding = player.getFunding();

        player.upgradeLand(land);
        assertThat(originalFunding - player.getFunding(), is(land.getLandPrice()));
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void upgradeLand_should_throw_exception_if_land_is_already_highest_level() throws Exception {
        expectedException.expect(CannotAccessLandException.class);
        expectedException.expectMessage("can not upgrade land with highest level");

        Land land = createLandWithOwner(player);
        when(game.getLocation(anyInt())).thenReturn(land);

        player.upgradeLand(land);
        player.upgradeLand(land);
        player.upgradeLand(land);
        player.upgradeLand(land);
    }

    @Test(expected = NoEnoughFoundException.class)
    public void upgradeLand_should_throw_exception_if_player_funding_is_not_enough() throws Exception {
        Player veryPoolPlayer = Player.createPlayer(game, 1, 20);
        Land land = createLandWithOwner(veryPoolPlayer);
        veryPoolPlayer.upgradeLand(land);
    }

    @Test
    public void should_update_user_land_info_after_manipulate_land() throws Exception {
        player.buyLand(new Land(100));
        assertThat(player.countOfLandWithLevel(Land.EMPTY_LAND), is(1));
        Land land = new Land(300);
        player.buyLand(land);
        assertThat(player.countOfLandWithLevel(Land.EMPTY_LAND), is(2));
        player.upgradeLand(land);
        assertThat(player.countOfLandWithLevel(Land.LEVEL_ONE), is(1));
    }

    @Test
    public void should_decrease_money_when_arrive_at_others_land() throws Exception {
        Player player = Player.createPlayer(game, 1);
        Land land = new Land(300);
        land.setOwner(player);

        Player otherPlayer = Player.createPlayer(game, 2);
        int originalFunding = otherPlayer.getFunding();
        land.process(otherPlayer);
        assertThat(originalFunding - otherPlayer.getFunding(), is(150));

        player.upgradeLand(land);
        originalFunding = otherPlayer.getFunding();
        land.process(otherPlayer);
        assertThat(originalFunding - otherPlayer.getFunding(), is(300));
    }

    @Test
    public void should_set_unpunish_roll_to_5_after_get_god() throws Exception {
        player.getGod();
        assertThat(player.getUnpunishRoll(), is(5));
    }

    @Test
    public void can_sell_tool() throws Exception {
        player.addTool(Tool.BOMB);
        player.sellTool(Tool.BOMB);
        assertThat(player.getPoint(), is(Tool.BOMB.getPrice()));
        assertThat(player.getCountOf(Tool.BOMB), is(0));
    }

    @Test
    public void can_sell_land() throws Exception {
        int originalFunding = player.getFunding();
        Land land = new Land(100);
        player.buyLand(land);
        when(game.getLocation(10)).thenReturn(land);
        player.sell(10);
        assertThat(player.getFunding(), is(originalFunding));
        assertThat(player.getLandInfo(), not(containsString("1")));
    }

    @Test
    public void should_update_current_player_location_after_roll() throws Exception {
        when(dice.getInt()).thenReturn(5);
        when(game.getLocation(anyInt())).thenReturn(new Land(100));
        Player player = Player.createPlayer(game, 1);
        int player1OriginalLocation = player.getLocationIndex();
        player.roll();
        assertThat(player.getLocationIndex() - player1OriginalLocation, is(5));
    }

    private Land createLandWithOwner(Player player) {
        Land land = new Land(200);
        land.setOwner(player);
        return land;
    }
}