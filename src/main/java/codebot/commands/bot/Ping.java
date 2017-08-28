package codebot.commands.bot;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;

class Ping extends Command {
    Ping() {
        name = "ping";
        info = "Returns ping of the bot";
        usage.add("");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        return send(message.getTextChannel(), id, "Ping: " + message.getJDA().getPing() + "ms");
    }
}
