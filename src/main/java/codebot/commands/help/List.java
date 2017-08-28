package codebot.commands.help;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;

import java.util.Collections;

import static codebot.Listener.main;

class List extends Command {
    List() {
        name = "list";
        info = "Lists all commands and their children";
        usage.add("");
        usage.add("\"perms\"");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        boolean b = args.length > 0 && args[0].toLowerCase().equals("perms");
        StringBuilder sb = new StringBuilder("```\n");
        sb = loop(main, sb, -1, perms, b);
        sb.append("```");
        return send(message.getTextChannel(), id, sb.toString());
    }


    private StringBuilder loop(Command c, StringBuilder sb, int level, byte perms, boolean b) {
        if (level != -1 && b) sb.append(c.permLevel).append(" ");
        for (int i = 0; i < level - (b ? 0 : 1); i++) sb.append("  ");
        if (level != -1) {
            if (!b && level != 0) sb.append(perms < c.permLevel ? "\uD83D\uDD12 " : "  ");
            sb.append(c.name).append("\n");
        }
        if (c.children.size() > 0) {
            for (Command i : c.children) sb = loop(i, sb, level + 1, perms, b);
        }
        return sb;
    }
}
