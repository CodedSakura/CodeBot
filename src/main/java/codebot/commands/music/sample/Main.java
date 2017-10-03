package codebot.commands.music.sample;

import codebot.Command;

public class Main extends Command {
    Main() {
        name = "sample";
        info = "Hosts sample system";
        children.add(new Play());
    }
}
