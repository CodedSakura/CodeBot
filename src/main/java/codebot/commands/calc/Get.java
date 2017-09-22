package codebot.commands.calc;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;

class Get extends Command {
    Get() {
        name = "get";
        info = "Gets a variable";

        usage.add("name");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        if (Main.parser.variables.keySet().contains(args[0])) {
            return send(message.getTextChannel(), id, Main.parser.variables.get(args[0]));
        } else {
            return send(message.getTextChannel(), id, "ERR: variable \"" + args[0] + "\" is not set");
        }
    }
}
