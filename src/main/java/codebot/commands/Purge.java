package codebot.commands;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;

import java.util.List;

class Purge extends Command {
    Purge() {
        name = "purge";
        info = "Deletes specified amount of messages";
        usage.add("amount");
        permLevel = 1;
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        try {
            int amount = 0;
            amount += Integer.parseInt(args[0]) + 1;
            if (id != null) amount ++;
            amount = Math.min(Math.max(amount, 1), 100);
            List<Message> h = message.getTextChannel().getHistory().retrievePast(amount).complete();
            message.getTextChannel().deleteMessages(h).queue();
            return null;
        } catch (NumberFormatException e) {
            return send(message.getTextChannel(), id, args[0] + " is not a number!");
        }
    }
}
