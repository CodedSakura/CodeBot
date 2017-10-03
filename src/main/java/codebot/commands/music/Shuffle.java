package codebot.commands.music;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;

class Shuffle extends Command {
    Shuffle() {
        name = "shuffle";
        info = "Shuffles the queue";
        usage.add("");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        Main.handler.shuffle();
        return send(message.getTextChannel(), id, "Shuffled");
    }
}