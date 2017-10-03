package codebot.commands.music;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;

class Disconnect extends Command {
    Disconnect() {
        name = "disconnect";
        info = "Disconnects from the voice channel";
        usage.add("");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        message.getGuild().getAudioManager().closeAudioConnection();
        return send(message.getTextChannel(), id, "Disconnected");
    }
}