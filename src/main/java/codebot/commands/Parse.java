package codebot.commands;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;
import parser.Parser;

class Parse extends Command {
    private static final Parser parser = new Parser();

    Parse() {
        name = "parse";
        info = "Parses math";
        usage.add("math");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        try {
            return send(message.getTextChannel(), id, parser.parse(args[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return send(message.getTextChannel(), id, "ERR: " + e.toString());
        }
    }
}
