package codebot.commands.music;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.List;

class Connect extends Command {
    Connect() {
        name = "connect";
        info = "Connects to a voice channel";
        usage.add("");
        usage.add("id");
        usage.add("name");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        if (!args[0].equals("")) {
            List<VoiceChannel> channels = message.getGuild().getVoiceChannelsByName(args[0], true);
            VoiceChannel channel;
            if (channels.size() > 0) channel = channels.get(0);
            else if (message.getGuild().getVoiceChannelById(args[0]) != null) {
                channel = message.getGuild().getVoiceChannelById(args[0]);
            } else {
                return send(message.getTextChannel(), id, "ERR: channel not found");
            }
            return Main.handler.establishConnection(channel, message.getTextChannel(), id);
        } else if (message.getMember().getVoiceState().inVoiceChannel()) {
            return Main.handler.establishConnection(message.getMember().getVoiceState().getChannel(), message.getTextChannel(), id);
        } else {
            return send(message.getTextChannel(), id, "ERR: you are not in a voice channel, nor did you provide a name or an id");
        }
    }
}
