package codebot.commands.music;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;

class Pause extends Command {
    Pause() {
        name = "pause";
        info = "Pauses nyan cat";
        usage.add("");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        return send(message.getTextChannel(), id, "Player is now " + (Main.handler.pause() ? "paused" : "resumed"));
    }
}