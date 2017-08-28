package codebot.commands;

import codebot.Command;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CommandTest {
    @Test
    public void hasChild() throws Exception {
        Command main = new Main();
        String[] commands = {"bot", "pong"};
        Map<String, String> alias = new HashMap<>();
        alias.put("pong", "ping");
        assertEquals(true, main.hasChild(commands, alias));
    }

}