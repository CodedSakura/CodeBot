package codebot.commands.music;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;

class Play extends Command {
    Play() {
        name = "play";
        info = "Plays nyan cat";
        usage.add("");
        usage.add("url");
        usage.add("youtube query");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        Main.handler.pause(false);
        if (!args[0].equals("")) {
            if (!args[0].matches("https?://.*")) args[0] = "ytsearch:" + args[0];
            return Main.handler.loadAndPlay(args[0], message.getTextChannel(), id);
        } else {
            return send(message.getTextChannel(), id, "Player is now playing");
        }
    }
}
