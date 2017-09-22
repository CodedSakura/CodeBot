package codebot.commands.calc;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;
import parser.error.ParserException;

import java.util.Map;

class Set extends Command {
    Set() {
        name = "set";
        info = "Sets a variable";

        usage.add("name, value");
        usage.add("name, expression");
        usage.add("name, \"null\"");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        if (args[1].toLowerCase().equals("null")) {
            if (Main.parser.variables.keySet().contains(args[0])) {
                Main.parser.variables.remove(args[0]);
                return send(message.getTextChannel(), id, "success");
            } else {
                return send(message.getTextChannel(), id, "ERR: variable \"" + args[0] + "\" not set");
            }
        } else {
            try {
                Main.parser.parse(args[1]);
                if (Main.parser.variables.keySet().contains(args[0])) {
                    Main.parser.variables.replace(args[0], args[1]);
                } else {
                    Main.parser.variables.put(args[0], args[1]);
                }
                return send(message.getTextChannel(), id, "success");
            } catch (NumberFormatException e) {
                return send(message.getTextChannel(), id, "ERR: NumberFormatException");
            } catch (ParserException e) {
                return send(message.getTextChannel(), id, "ERR: " + e.getMessage());
            }
        }
    }
}
