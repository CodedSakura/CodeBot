package codebot.commands.music;

import codebot.Command;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.entities.Message;

class Current extends Command {
    Current() {
        name = "current";
        info = "Shows the current song";
        usage.add("");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        Object[] currentInfo = Main.handler.currentInfo();
        if (currentInfo == null) return send(message.getTextChannel(), id, "The player is currently not playing anything");
        AudioTrackInfo info = (AudioTrackInfo) currentInfo[0];
        String position = Main.time((Long) currentInfo[1]);
        String out =
                "title: " + (info.title == null ? "-" : info.title) + "\n" +
                "author: " + (info.author == null ? "-" : info.author) + "\n" +
                "duration: " + (info.isStream ? "stream" : Main.time(info.length)) + "\n" +
                "current position: " + (info.isStream ? "stream" : position) + "\n" +
                "uri: " + info.uri;
        return send(message.getTextChannel(), id, out);
    }
}