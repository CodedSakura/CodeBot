package codebot.commands.user;

import codebot.Command;

public class Main extends Command {
    public Main() {
        name = "user";
        info = "User management";
        children.add(new Warn());
        children.add(new Pardon());
        children.add(new Ban());
        children.add(new Kick());
    }
}
