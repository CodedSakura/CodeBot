package codebot.commands;


import codebot.Command;

public class Main extends Command {
    public Main() {
        children.add(new codebot.commands.bot.Main());
        children.add(new codebot.commands.help.Main());
        children.add(new codebot.commands.role.Main());
        children.add(new codebot.commands.user.Main());
        children.add(new codebot.commands.random.Main());
        children.add(new Latex());
        children.add(new Purge());
        children.add(new Info());
    }
}
