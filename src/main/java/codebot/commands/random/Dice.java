package codebot.commands.random;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;

import java.util.concurrent.ThreadLocalRandom;

class Dice extends Command {
    Dice() {
        name = "dice";
        info = "With this you can roll a d1 or a d13 or whatever (default is d6) :D";
        usage.add("");
        usage.add("sides");
        usage.add("sides, count");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        int sides = 6;
        int times = 1;
        try { if (!args[0].equals("")) sides = new Integer(args[0]); } catch (NumberFormatException e) {
            return send(message.getTextChannel(), id, "ERR: \"" + args[0] + "\" is not a valid number");
        }
        try { if (args.length > 1) times = new Integer(args[1]); } catch (NumberFormatException e) {
            return send(message.getTextChannel(), id, "ERR: \"" + args[2] + "\" is not a valid number");
        }
        if (times < 0) return send(message.getTextChannel(), id, "ERR: \"times\" should be bigger than 0");
        StringBuilder sb = new StringBuilder("Result");
        sb.append(SOrNoS(times)).append(":\n");
        for (int i = 0; i < times; i++) sb.append(ThreadLocalRandom.current().nextInt(1, sides+1)).append(", ");
        sb.delete(sb.length() - 2, sb.length());
        return send(message.getTextChannel(), id, sb.toString());
    }
}
