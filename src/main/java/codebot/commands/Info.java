package codebot.commands;

import codebot.Command;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.Role;

import java.time.format.DateTimeFormatter;

class Info extends Command {
    Info() {
        name = "info";
        info = "Gives info about supplied entry";
        usage.add("@mention");
        usage.add("#channel");
        usage.add("@role");
        usage.add(":emote:");
        usage.add("nickname");
        usage.add("role name");
        usage.add("channel name");
        usage.add("emote name");
        usage.add("id");
        usage.add("\"guild\"");
        usage.add("\"self\"");
        usage.add("\"user\"");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        String out = "";
        boolean possibleID = args[0].matches("\\d{17,19}");
             if (message.getMentionedUsers().size() > 0) {
            if (message.getMentionedUsers().size() > 1) out += "Analysing 1st mention\n";
            out += info(message.getGuild().getMember(message.getMentionedUsers().get(0)));
        }
        else if (message.getMentionedRoles().size() > 0) {
            if (message.getMentionedRoles().size() > 1) out += "Analysing 1st mention\n";
            out += info(message.getMentionedRoles().get(0));
        }
        else if (message.getMentionedChannels().size() > 0) {
            if (message.getMentionedChannels().size() > 1) out += "Analysing 1st mention\n";
            out += info(message.getMentionedChannels().get(0));
        }
        else if (message.getEmotes().size() > 0) {
            if (message.getEmotes().size() > 1) out += "Analysing 1st emote\n";
            out += info(message.getEmotes().get(0));
        }
        else if (message.getGuild().getMembersByEffectiveName(args[0], true).size() > 0) out += info(message.getGuild().getMembersByEffectiveName(args[0], true).get(0));
        else if (message.getGuild().getRolesByName(args[0], true).size() > 0) out += info(message.getGuild().getRolesByName(args[0], true).get(0));
        else if (message.getGuild().getTextChannelsByName(args[0], true).size() > 0) out += info(message.getGuild().getTextChannelsByName(args[0], true).get(0));
        else if (message.getGuild().getVoiceChannelsByName(args[0], true).size() > 0) out += info(message.getGuild().getVoiceChannelsByName(args[0], true).get(0));
        else if (message.getGuild().getEmotesByName(args[0], true).size() > 0) out += info(message.getGuild().getEmotesByName(args[0], true).get(0));
        else if (possibleID && message.getGuild().getMemberById(args[0]) != null) out += info(message.getGuild().getMemberById(args[0]));
        else if (possibleID && message.getGuild().getRoleById(args[0]) != null) out += info(message.getGuild().getRoleById(args[0]));
        else if (possibleID && message.getGuild().getTextChannelById(args[0]) != null) out += info(message.getGuild().getTextChannelById(args[0]));
        else if (possibleID && message.getGuild().getVoiceChannelById(args[0]) != null) out += info(message.getGuild().getVoiceChannelById(args[0]));
        else if (possibleID && message.getGuild().getEmoteById(args[0]) != null) out += info(message.getGuild().getEmoteById(args[0]));
        else if (args[0].toLowerCase().equals("guild")) out += info(message.getGuild());
        else if (args[0].toLowerCase().equals("user") || args[0].toLowerCase().equals("self")) out += info(message.getMember());
        else out = "Nothing found!";
        return send(message.getTextChannel(), id, out);
    }

    private String info(Member m) {
        String[] t = {""};
        m.getRoles().forEach(role -> t[0] += role.getName() + ", ");
        t[0] = t[0].substring(0, t[0].length() - 2);
        return  "```" +
                "name           " + m.getUser().getName() + "\n" +
                "discriminator  #" + m.getUser().getDiscriminator() + "\n" +
                "id             " + m.getUser().getId() + "\n" +
                "is a bot       " + m.getUser().isBot() + "\n" +
                "created        " + m.getUser().getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME) + "\n" +
                "is owner       " + m.isOwner() + "\n" +
                "role amount    " + m.getRoles().size() + "\n" +
                "roles          " + t[0] + "\n" +
                "effect. name   " + m.getEffectiveName() + "\n" +
                "status         " + m.getOnlineStatus().getKey().toLowerCase() + "\n" +
                "game           " + (m.getGame() != null ? m.getGame().getName() : "none") + "\n" +
                "join date      " + m.getJoinDate().format(DateTimeFormatter.RFC_1123_DATE_TIME) + "\n" +
                "```" + m.getUser().getAvatarUrl();
    }
    private String info(Role r) {
        String[] t = {""};
        r.getPermissions().forEach(p -> t[0] += p.getName().toLowerCase() + ", ");
        t[0] = t[0].substring(0, t[0].length() - 2);
        return  "```" +
                "name         " + r.getName() + "\n" +
                "id           " + r.getId() + "\n" +
                "created      " + r.getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME) + "\n" +
                "position     " + r.getPosition() + "\n" +
                "color        #" + Integer.toHexString(r.getColor().getRGB()).substring(2).toUpperCase() + "\n" +
                "mentionable  " + r.isMentionable() + "\n" +
                "permissions  " + t[0] + "\n" +
                "members      " + r.getGuild().getMembersWithRoles(r).size() + "" +
                "```";
    }
    private String info(TextChannel c) {
        return  "```" +
                "name       " + c.getName() + "\n" +
                "id         " + c.getId() + "\n" +
                "topic      " + c.getTopic() + "\n" +
                "position   " + c.getPosition() + "\n" +
                "created    " + c.getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME) + "\n" +
                "is NSFW    " + c.isNSFW() + "\n" +
                "pin count  " + c.getPinnedMessages().complete().size() + "\n" +
                "```";
    }
    private String info(VoiceChannel c) {
        return  "```" +
                "name          " + c.getName() + "\n" +
                "id            " + c.getId() + "\n" +
                "position      " + c.getPosition() + "\n" +
                "user limit    " + c.getUserLimit() + "\n" +
                "bitrate       " + c.getBitrate() + "\n" +
                "member count  " + c.getMembers().size() + "\n" +
                "created       " + c.getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME) + "\n" +
                "```";
    }
    private String info(Emote e) {
        return  "```" +
                "name     " + e.getName() + "\n" +
                "id       " + e.getId() + "\n" +
                "created  " + e.getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME) + "\n" +
                "managed  " + e.isManaged() + "\n" +
                "fake     " + e.isFake() + "\n" +
                "```" + e.getImageUrl();
    }
    private String info(Guild g) {
        return  "```" +
                "name                " + g.getName() + "\n" +
                "id                  " + g.getId() + "\n" +
                "created             " + g.getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME) + "\n" +
                "verified            " + g.checkVerification() + "\n" +
                "role amount         " + g.getRoles().size() + "\n" +
                "emote amount        " + g.getEmotes().size() + "\n" +
                "voice AFK channel   " + (g.getAfkChannel() != null ? g.getAfkChannel().getName() : "none") + "\n" +
                "voice AFK timeout   " + g.getAfkTimeout().getSeconds() + " seconds\n" +
                "member amount       " + g.getMembers().size() + "\n" +
                "owner               " + g.getOwner().getEffectiveName() + "\n" +
                "default notif. lvl  " + g.getDefaultNotificationLevel().getKey() + "\n" +
                "verification lvl    " + g.getVerificationLevel().getKey() + "\n" +
                "region              " + g.getRegion().getName() + "\n" +
                "required MFA level  " + g.getRequiredMFALevel().getKey() + "\n" +
                "```" + g.getIconUrl();
    }
}
