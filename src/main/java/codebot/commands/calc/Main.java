package codebot.commands.calc;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;
import parser.Parser;
import parser.error.ParserException;

public class Main extends Command {
    static final Parser parser = new Parser();

    public Main() {
        name = "calc";
        info = "Calculates math";

        usage.add("math");

        children.add(new Set());
        children.add(new Get());
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        try {
            return send(message.getTextChannel(), id, parser.parse(args[0]));
        } catch (NumberFormatException e) {
            return send(message.getTextChannel(), id, "ERR: NumberFormatException");
        } catch (ParserException e) {
            return send(message.getTextChannel(), id, "ERR: " + e.getMessage());
        }
    }
}