package codebot.commands.music;

import codebot.Command;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.entities.Message;

class Queue extends Command {
    Queue() {
        name = "queue";
        info = "Shows the queue";
        usage.add("");
        usage.add("full");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        AudioTrackInfo[] info = Main.handler.queueInfo();
        if (info == null || info[0] == null) return send(message.getTextChannel(), id, "The queue is empty");
        long totalLength = 0;
        for (AudioTrackInfo i : info) if (!i.isStream) totalLength += i.length;
        StringBuilder out = new StringBuilder("length: " + info.length + "\nduration: " + Main.time(totalLength) + "\n");
        if (args[0].toLowerCase().equals("full")) {
            for (AudioTrackInfo i : info) {
                out.append("title: ").append(i.title).append("; duration: ").append(Main.time(i.length)).append("\n");
            }
        } else {
            for (int i = 0; i < Math.max(10, info.length); i++) {
                out.append("title: ").append(info[i].title).append("; duration: ").append(Main.time(info[i].length)).append("\n");
            }
        }
        return send(message.getTextChannel(), id, out.toString());
    }
}