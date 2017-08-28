package codebot.commands.bot;

import codebot.Command;

public class Main extends Command {
    public Main() {
        name = "bot";
        info = "Bot";
        children.add(new Ping());
        children.add(new Eval());
        children.add(new Reset());
        children.add(new codebot.commands.bot.config.Main());
    }
}
