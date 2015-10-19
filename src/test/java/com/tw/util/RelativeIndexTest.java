package com.tw.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class RelativeIndexTest {
    @Test
    public void should_get_relative_index() throws Exception {
        assertThat(RelativeIndex.get(0, -10, 60), is(50));
        assertThat(RelativeIndex.get(55, 10, 60), is(5));
    }
}