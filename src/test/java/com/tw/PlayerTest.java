package com.tw;

import com.tw.exception.CannotAccessLandException;
import com.tw.exception.NoEnoughFoundException;
import com.tw.location.Land;
import com.tw.location.Location;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerTest {

    private Game game;

    @Before
    public void setUp() throws Exception {
        game = mock(Game.class);
        when(game.getMapSize()).thenReturn(70);
    }

    @Test
    public void should_get_location_0_for_all_users() throws Exception {
        assertThat(Player.createPlayer(game, 1).getLocationIndex(), is(0));
        assertThat(Player.createPlayer(game, 2).getLocationIndex(), is(0));
    }

    @Test
    public void should_get_type_of_land() throws Exception {
        when(game.location(anyInt())).thenReturn(new Land(200));
        Player player = Player.createPlayer(game, 1);
        assertThat(player.getCurrentLocation().isLand(), is(true));
    }

    @Test
    public void should_upgrade_level_by_1_after_upgrade() throws Exception {
        Player player = Player.createPlayer(game, 1);
        Land land = createLandWithOwner(player);
        when(game.location(anyInt())).thenReturn(land);
        assertThat(land.getLevel(), is(0));

        player.upgradeLand();

        assertThat(land.getLevel(), is(1));
    }

    @Test
    public void upgradeLand_should_decrease_same_money_as_buy_the_land_after_upgrade() throws Exception {
        Player player = Player.createPlayer(game, 1);
        Land land = createLandWithOwner(player);
        when(game.location(anyInt())).thenReturn(land);
        int originalFunding = player.getFunding();

        player.upgradeLand();
        assertThat(originalFunding - player.getFunding(), is(land.getPrice()));
    }

    @Test
    public void upgradeLand_should_throw_exception_is_the_land_is_owned_by_others() throws Exception {
        expectedException.expect(CannotAccessLandException.class);
        expectedException.expectMessage("can not upgrade land which is not belong to you");
        Player player1 = Player.createPlayer(game, 1);
        Player player2 = Player.createPlayer(game, 2);

        Location landOwnByOthers = createLandWithOwner(player2);

        when(game.location(anyInt())).thenReturn(landOwnByOthers);

        player1.upgradeLand();
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void upgradeLand_should_throw_exception_if_land_is_already_highest_level() throws Exception {
        expectedException.expect(CannotAccessLandException.class);
        expectedException.expectMessage("can not upgrade land with highest level");

        Player player = Player.createPlayer(game, 1);
        Land land = createLandWithOwner(player);
        when(game.location(anyInt())).thenReturn(land);

        player.upgradeLand();
        player.upgradeLand();
        player.upgradeLand();
        player.upgradeLand();
    }

    @Test(expected = NoEnoughFoundException.class)
    public void upgradeLand_should_throw_exception_if_player_funding_is_not_enough() throws Exception {
        Player player = Player.createPlayer(game, 1, 20);
        Land land = createLandWithOwner(player);
        when(game.location(anyInt())).thenReturn(land);

        player.upgradeLand();
    }

    @Test
    public void should_update_user_land_info_after_manipulate_land() throws Exception {
        Player player = Player.createPlayer(game, 1);
        when(game.location(anyInt())).thenReturn(new Land(100));
        player.buyEmptyLand();
        assertThat(player.countOfEmptyLand(), is(1));
        when(game.location(anyInt())).thenReturn(new Land(300));
        player.buyEmptyLand();
        assertThat(player.countOfEmptyLand(), is(2));
    }

    private Land createLandWithOwner(Player player) {
        Land land;
        land = new Land(200);
        land.setOwner(player);
        return land;
    }
}