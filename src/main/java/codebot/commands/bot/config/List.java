package codebot.commands.bot.config;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;

class List extends Command {
    List() {
        name = "list";
        info = "Lists all config parameters";
        usage.add("");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        String out = "```\n" +
                "prefix\n" +
                "limit\n" +
                "latex\n" +
                "#readme\n" +
                "#admin\n" +
                "#default\n" +
                "ignored users\n" +
                "ignored roles\n" +
                "lvl1 users\n" +
                "lvl1 roles\n" +
                "lvl2 users\n" +
                "lvl2 roles\n" +
                "@admin\n" +
                "@bot\n" +
                "@member\n" +
                "@blacklist\n" +
                "alias\n" +
                "```";
        return send(message.getTextChannel(), id, out);
    }
}
