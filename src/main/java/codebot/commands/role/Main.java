package codebot.commands.role;

import codebot.Command;

public class Main extends Command {
    public Main() {
        name = "role";
        info = "Hosts role commands";
        children.add(new Get());
        children.add(new Set());
        children.add(new List());
    }
}
