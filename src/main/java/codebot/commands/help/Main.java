package codebot.commands.help;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;

import static codebot.Listener.alias;
import static codebot.Listener.main;

public class Main extends Command {
    public Main() {
        name = "help";
        info = "Prints help about selected command";
        usage.add("");
        usage.add("command name");
        children.add(new List());
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        if (args[0].equals("")) {
            StringBuilder s = new StringBuilder(info + "\n\nUsage:\n");
            for (String i : usage) s.append(name).append("(").append(i).append(")\n");
            s.append("\nChild commands:");
            for (Command i : children) s.append("`").append(i.name).append("`").append(", ");
            s.delete(s.length() - 2, s.length());
            return send(message.getTextChannel(), id, s.toString());
        } else {
            if (main.hasChild(args[0].split("\\."), alias)) {
                Command c = main.getChild(args[0].split("\\."), alias);
                StringBuilder s = new StringBuilder(c.info + "\n\n");
                if (c.usage.size() > 0) {
                    s.append("Usage:\n");
                    for (String i : c.usage) s.append(args[0]).append("(").append(i).append(")\n");
                    s.append("\n");
                }
                if (c.children.size() > 0) {
                    s.append("Child commands:\n");
                    for (Command i : c.children) s.append("`").append(i.name).append("`").append(", ");
                    s.delete(s.length() - 2, s.length());
                }
                return send(message.getTextChannel(), id, s.toString());
            } else {
                return send(message.getTextChannel(), id, "ERR: unknown command");
            }
        }
    }
}
