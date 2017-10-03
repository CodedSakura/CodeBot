package codebot.commands;


import codebot.Command;
import net.dv8tion.jda.core.entities.Guild;

public class Main extends Command {
    public Main(Guild guild) {
        children.add(new codebot.commands.bot.Main());
        children.add(new codebot.commands.calc.Main());
        children.add(new codebot.commands.help.Main());
        children.add(new codebot.commands.music.Main(guild));
        children.add(new codebot.commands.random.Main());
        children.add(new codebot.commands.role.Main());
        children.add(new codebot.commands.user.Main());
        children.add(new Convert());
        children.add(new Info());
        children.add(new Latex());
        children.add(new Purge());
        children.add(new Time());
    }
}
