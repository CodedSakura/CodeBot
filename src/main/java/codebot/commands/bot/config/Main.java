package codebot.commands.bot.config;

import codebot.Command;

public class Main extends Command {
    public Main() {
        name = "config";
        info = "Parent of commands that have to do with the bot's config";
        children.add(new Reload());
        children.add(new List());
        children.add(new Get());
    }
}
