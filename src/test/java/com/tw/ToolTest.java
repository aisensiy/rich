package com.tw;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ToolTest {
    @Test
    public void should_show_string_for_to_string() throws Exception {
        assertThat(Tool.ROADBLOCK.toString(), is("路障\t1\t50\t#"));
        assertThat(Tool.ROBOT.toString(), is("机器娃娃\t2\t30\t"));
        assertThat(Tool.BOMB.toString(), is("炸弹\t3\t50\t@"));
    }
}