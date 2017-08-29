package codebot.commands.random;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;

import java.util.concurrent.ThreadLocalRandom;

public class Main extends Command {
    public Main() {
        name = "random";
        info = "Generates randomness (min=1, max=10, both inclusive)";
        usage.add("");
        usage.add("min");
        usage.add("min, max");
        usage.add("min, max, times");
        children.add(new Dice());
        children.add(new EightBall());
        children.add(new Flip());
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        int min = 1;
        int max = 10;
        int times = 1;
        try { if (!args[0].equals("")) min = new Integer(args[0]); } catch (NumberFormatException e) {
            return send(message.getTextChannel(), id, "ERR: \"" + args[0] + "\" is not a valid number");
        }
        try { if (args.length > 1) max = new Integer(args[1]); } catch (NumberFormatException e) {
            return send(message.getTextChannel(), id, "ERR: \"" + args[1] + "\" is not a valid number");
        }
        try { if (args.length > 2) times = new Integer(args[2]); } catch (NumberFormatException e) {
            return send(message.getTextChannel(), id, "ERR: \"" + args[2] + "\" is not a valid number");
        }
        if (times < 0) return send(message.getTextChannel(), id, "ERR: \"times\" should be bigger than 0");
        StringBuilder sb = new StringBuilder("Your random number");
        sb.append(s(times) ? " is" : "s are").append(":\n");
        for (int i = 0; i < times; i++) {
            sb.append(ThreadLocalRandom.current().nextInt(min, max+1)).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return send(message.getTextChannel(), id, sb.toString());
    }

    private boolean s(int num) {
        return (num % 10 == 1 && num % 100 != 11);
    }
}
