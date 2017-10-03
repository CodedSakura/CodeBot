package codebot.commands.music;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;

class Skip extends Command {
    Skip() {
        name = "skip";
        info = "Skips to the next track";
        usage.add("");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        return Main.handler.skipTrack(message.getTextChannel(), id);
    }
}