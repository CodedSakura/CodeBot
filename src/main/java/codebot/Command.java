package codebot;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Command {
    public String name;
    public String info = "";
    public List<String> usage = new ArrayList<>();
    public List<Command> children = new ArrayList<>();
    public byte permLevel = 0;

    String checkAndRun(Message message, String[] args, byte perms, String id) {
        if (permLevel > perms) return null;
        return run(message, args, perms, id);
    }
    public String run(Message message, String[] args, byte perms, String id) {
        if (children.size() == 0) return null;
        StringBuilder out = new StringBuilder(info + "\n\nChild commands:");
        for (Command c : children) out.append("`").append(c.name).append("`, ");
        out.delete(out.length() - 2, out.length());
        return send(message.getTextChannel(), id, out.toString());
    }

    public boolean hasChild(String[] name, Map<String, String> alias) {
        return getChild(name, alias) != null;
    }
    public Command getChild(String[] name, Map<String, String> alias) {
        List<String> n = new ArrayList<>();
        n.addAll(Arrays.asList(name));
        for (Command c : children) {
            if (alias != null && alias.containsKey(n.get(0))) n.set(0, alias.get(n.get(0)));
            if (c.name.equals(n.get(0))) {
                if (n.size() == 1) return c;
                n.remove(0);
                return c.getChild(n.toArray(new String[n.size()]), alias);
            }
        }
        return null;
    }

    public String send(TextChannel c, String id, String m) {
        MessageBuilder mb = new MessageBuilder();
        mb.append(m);
        return send(c, id, mb.build());
    }
    public String send(TextChannel c, String id, MessageEmbed m) {
        MessageBuilder mb = new MessageBuilder();
        mb.setEmbed(m);
        return send(c, id, mb.build());
    }
    public String send(TextChannel c, String id, Message m) {
        if (id == null) {
            return c.sendMessage(m).complete().getId();
        } else {
            c.editMessageById(id, m).queue();
        }
        return null;
    }

    public String SOrNoS(int num) {
        return (num % 10 == 1 && num % 100 != 11) ? "" : "s";
    }
}
