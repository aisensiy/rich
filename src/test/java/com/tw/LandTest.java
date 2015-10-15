package com.tw;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class LandTest {
    @Test
    public void should_get_different_name_of_land_with_different_level() throws Exception {
        Land land = new Land();
        assertThat(land.getName(), is("空地"));

        land.upgradeLevel();
        assertThat(land.getName(), is("茅屋"));

        land.upgradeLevel();
        assertThat(land.getName(), is("洋房"));

        land.upgradeLevel();
        assertThat(land.getName(), is("摩天楼"));
    }
}