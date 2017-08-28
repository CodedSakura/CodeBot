package codebot;

import codebot.commands.Main;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.DisconnectEvent;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.json.JSONObject;

import java.util.*;

public class Listener extends ListenerAdapter {
    public static Map<String, String> latest = new HashMap<>();
    public static Map<String, String> alias = new HashMap<>();
    public static JSONObject config = new JSONObject(FileIO.read("config.json"));
    public static JSONObject channels = config.getJSONObject("channels");
    public static JSONObject roles = config.getJSONObject("roles");
    public static JSONObject perms = config.getJSONObject("perms");
    public static JSONObject ignored = perms.getJSONObject("ignored");
    public static JSONObject lvl1 = perms.getJSONObject("level 1");
    public static JSONObject lvl2 = perms.getJSONObject("level 2");
    public static JSONObject cfgAlias = config.getJSONObject("alias");
    public static String prefix = config.getString("prefix");
    public static int limit = config.getInt("limit");
    public static Command main = new Main();

    Listener() {
        for (String i : cfgAlias.keySet()) alias.put(i, cfgAlias.getString(i));

        System.out.println("Using prefix: " + prefix);
    }

    @Override
    public void onReady(ReadyEvent event) {
        event.getJDA().getPresence().setGame(Game.of(config.getString("game")));
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Message message = event.getMessage();
        String id = message.getId();
        String content = message.getRawContent();
        SelfUser jda = event.getJDA().getSelfUser();
        TextChannel c = event.getChannel();

        if (!content.startsWith(prefix) && !content.startsWith(jda.getAsMention())) return;
        content = content.substring(content.startsWith(prefix) ? prefix.length() : jda.getAsMention().length()).trim();

        byte perms = 0;
        if (ignored.getJSONArray("users").toList().contains(event.getAuthor().getId())) return;
        for (Role r : event.getMember().getRoles()) if (ignored.getJSONArray("roles").toList().contains(r.getId())) return;

        if (lvl1.getJSONArray("users").toList().contains(event.getAuthor().getId())) perms = 1;
        for (Role r : event.getMember().getRoles()) if (lvl1.getJSONArray("roles").toList().contains(r.getId())) perms = 1;

        if (lvl2.getJSONArray("users").toList().contains(event.getAuthor().getId())) perms = 2;
        for (Role r : event.getMember().getRoles()) if (lvl2.getJSONArray("roles").toList().contains(r.getId())) perms = 2;

        boolean valid =  content.indexOf("(") < content.lastIndexOf(")");
        if (!valid) {
            latest.put(id, c.sendMessage("ERR: problem with parenthesis").complete().getId());
            return;
        }

        String[] commands = content.substring(0, content.indexOf("(")).toLowerCase().split("\\.");
        String[] args = splitArgs(content.substring(content.indexOf("(") + 1, content.lastIndexOf(")")));

        if (main.hasChild(commands, alias)) latest.put(id, main.getChild(commands, alias).checkAndRun(message, args, perms, null));
        else latest.put(id, c.sendMessage("ERR: unknown command").complete().getId());
        for (String key : latest.keySet()) if (latest.get(key) == null) latest.remove(key);
        while (latest.size() > limit) {
            List<String> keys = new ArrayList<>();
            keys.addAll(latest.keySet());
            Collections.sort(keys);
            latest.remove(keys.get(0));
        }
    }
    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
        if (!latest.containsKey(event.getMessageId())) return;
        String id = latest.get(event.getMessageId());
        Message message = event.getMessage();
        String content = message.getRawContent();
        SelfUser jda = event.getJDA().getSelfUser();
        TextChannel c = event.getChannel();

        if (!content.startsWith(prefix) && !content.startsWith(jda.getAsMention())) return;
        content = content.substring(content.startsWith(prefix) ? prefix.length() : jda.getAsMention().length()).trim();

        byte perms = 0;
        if (ignored.getJSONArray("users").toList().contains(event.getAuthor().getId())) return;
        for (Role r : event.getMember().getRoles()) if (ignored.getJSONArray("roles").toList().contains(r.getId())) return;

        if (lvl1.getJSONArray("users").toList().contains(event.getAuthor().getId())) perms = 1;
        for (Role r : event.getMember().getRoles()) if (lvl1.getJSONArray("roles").toList().contains(r.getId())) perms = 1;

        if (lvl2.getJSONArray("users").toList().contains(event.getAuthor().getId())) perms = 2;
        for (Role r : event.getMember().getRoles()) if (lvl2.getJSONArray("roles").toList().contains(r.getId())) perms = 2;

        boolean valid =  content.indexOf("(") < content.lastIndexOf(")");
        if (!valid) {
            c.editMessageById(id, "ERR: problem with parenthesis").queue();
            return;
        }

        String[] commands = content.substring(0, content.indexOf("(")).toLowerCase().split("\\.");
        String[] args = splitArgs(content.substring(content.indexOf("(") + 1, content.lastIndexOf(")")));

        if (main.hasChild(commands, alias)) main.getChild(commands, alias).checkAndRun(message, args, perms, id);
        else c.editMessageById(id, "ERR: unknown command").queue();
    }

    private void voiceJoin(Channel c, Guild g, Member m) {
        if (c.getMembers().size() < 2) return;

        String name = c.getName().replace(" ", "-") + "-voice";

        if (g.getTextChannelsByName(name, true).size() == 0) {
            Channel ch = g.getController().createTextChannel(name).complete();

            ch.getMemberPermissionOverrides().forEach(q -> q.getManagerUpdatable().reset());
            ch.createPermissionOverride(g.getRoleById(roles.getString("member"))).setDeny (Permission.MESSAGE_READ).queue();
            ch.createPermissionOverride(g.getRoleById(roles.getString("admin"))) .setAllow(Permission.MESSAGE_READ).queue();
            ch.createPermissionOverride(g.getRoleById(roles.getString("bot")))   .setAllow(Permission.MESSAGE_READ).queue();

            c.getMembers().forEach(q -> ch.createPermissionOverride(q).setAllow(Permission.MESSAGE_READ).queue());
        } else {
            g.getTextChannelsByName(name, true).get(0).createPermissionOverride(m).setAllow(Permission.MESSAGE_READ).queue();
        }
    }
    private void voiceLeave(VoiceChannel ch, Guild g, Member m) {
        String name = ch.getName().replace(" ", "-") + "-voice";
        List<TextChannel> c = g.getTextChannelsByName(name, true);
        if (c.size() == 1){
            if (ch.getMembers().size() < 2) c.get(0).delete().queue();
            else c.get(0).getPermissionOverride(m).getManagerUpdatable().getPermissionOverride().delete().queue();
        }
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        voiceJoin(event.getChannelJoined(), event.getGuild(), event.getMember());
    }
    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        voiceLeave(event.getChannelLeft(), event.getGuild(), event.getMember());
    }
    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        voiceLeave(event.getChannelLeft(), event.getGuild(), event.getMember());
        voiceJoin(event.getChannelJoined(), event.getGuild(), event.getMember());
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if (event.getMember().getUser().isBot()) {
            event.getGuild().getController().addRolesToMember(event.getMember(), event.getGuild().getRoleById(roles.getString("bot"))).queue();
            event.getGuild().getTextChannelById(channels.getString("default")).sendMessage("A bot (" + event.getMember().getEffectiveName() + ") was added to the guild").queue();
        } else {
            event.getGuild().getController().addRolesToMember(event.getMember(), event.getGuild().getRoleById(roles.getString("member"))).queue();
            event.getGuild().getTextChannelById(channels.getString("default")).sendMessage("" +
                    "Welcome, " + event.getMember().getUser().getAsMention() + "! Please read <#" + channels.getString("readme") + ">!\n" +
                    "To get a language role just do `" + config.getString("role cmd") + "`\n" +
                    "To see a list of available roles, do `" + config.getString("roles cmd") + "`!"
            ).queue();
            event.getGuild().getTextChannelById(channels.getString("admin")).sendMessage(event.getMember().getEffectiveName() + " joined the guild!").queue();
        }
    }
    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        event.getGuild().getTextChannelById(channels.getString("admin")).sendMessage(event.getMember().getEffectiveName() + " left the guild!").queue();
    }

    private int countChar(String s, char c) {
        int out = 0;
        for (char i : s.toCharArray()) out += i == c ? 1 : 0;
        return out;
    }
    private String[] splitArgs(String s) {
        String[] out = s.split(config.getString("regex"));
        boolean valid = true;
        for (int i = 0; i < out.length; i++) {
            String o = out[i].trim();
            out[i] = o;
            if ((countChar(o, '\'') % 2 != 0) || (countChar(o, '"') % 2 != 0)) valid = false;
            if (!valid) System.out.println(o);
            if (o.startsWith("\"")) out[i] = o.substring(1, o.length() - 1);
        }
        return valid ? out : null;
    }
}
