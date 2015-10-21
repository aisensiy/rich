package com.tw.util;

import com.tw.util.Tool;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.*;

public class ToolTest {
    @Test
    public void should_show_string_for_to_string() throws Exception {
        assertThat(Tool.ROADBLOCK.toString(), is("路障\t1\t50\t#"));
        assertThat(Tool.ROBOT.toString(), is("机器娃娃\t2\t30\t"));
        assertThat(Tool.BOMB.toString(), is("炸弹\t3\t50\t@"));
    }

    @Test
    public void should_list_tools_in_string() throws Exception {
        assertThat(Tool.listTools(), startsWith("道具\t编号\t点数\t显示方式"));
    }
}