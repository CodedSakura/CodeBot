package codebot.commands.random;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;

import java.util.Random;

class Flip extends Command {
    Flip() {
        name = "flip";
        info = "Flips a coin, or two, or ten, or how many you want";
        usage.add("");
        usage.add("times");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        int times = 1;
        Random r = new Random();
        try { if (!args[0].equals("")) times = new Integer(args[0]); } catch (NumberFormatException e) {
            return send(message.getTextChannel(), id, "ERR: \"" + args[0] + "\" is not a valid number");
        }
        if (times < 0) return send(message.getTextChannel(), id, "ERR: \"times\" should be bigger than 0");
        StringBuilder sb = new StringBuilder("Result");
        sb.append(SOrNoS(times)).append(":\n");
        if (times <= 10) {
            for (int i = 0; i < times; i++) sb.append(r.nextBoolean() ? "heads" : "tails").append(", ");
            sb.delete(sb.length() - 2, sb.length());
        } else {
            for (int i = 0; i < times; i++) sb.append(r.nextBoolean() ? "h" : "t");
        }
        return send(message.getTextChannel(), id, sb.toString());
    }
}
