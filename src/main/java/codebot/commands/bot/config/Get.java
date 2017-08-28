package codebot.commands.bot.config;

import codebot.Command;
import net.dv8tion.jda.core.entities.*;
import org.json.JSONObject;

import java.util.List;

import static codebot.Listener.*;

class Get extends Command {
    Get() {
        name = "get";
        info = "Gets a config parameter";
        usage.add("key");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        TextChannel c = message.getTextChannel();
        Guild g = message.getGuild();
        switch (args[0]) {
            case "prefix":        return send(c, id, prefix);
            case "limit":         return send(c, id, "" + limit);
            case "latex":         return send(c, id, config.getString("latex"));
            case "#readme":       return send(c, id, g.getTextChannelById(channels.getString("readme")).getAsMention());
            case "#admin":        return send(c, id, g.getTextChannelById(channels.getString("admin")).getAsMention());
            case "#default":      return send(c, id, g.getTextChannelById(channels.getString("default")).getAsMention());
            case "ignored users": return send(c, id, users(ignored, g));
            case "ignored roles": return send(c, id, roles(ignored, g));
            case "lvl1 users":    return send(c, id, users(lvl1, g));
            case "lvl1 roles":    return send(c, id, roles(lvl1, g));
            case "lvl2 users":    return send(c, id, users(lvl2, g));
            case "lvl2 roles":    return send(c, id, roles(lvl2, g));
            case "@admin":        return send(c, id, role(g.getRoleById(roles.getString("admin"))));
            case "@bot":          return send(c, id, role(g.getRoleById(roles.getString("bot"))));
            case "@member":       return send(c, id, role(g.getRoleById(roles.getString("member"))));
            case "@blacklist":    return send(c, id, roles(roles, g, "blacklist"));
            case "alias":         return send(c, id, alias());
            default:              return send(message.getTextChannel(), id, "ERR: key not found");
        }
    }

    private String roles(JSONObject o, Guild g) {
        return roles(o, g, "roles");
    }
    private String roles(JSONObject o, Guild g, String key) {
        List<Object> roles = o.getJSONArray(key).toList();
        if (roles.size() == 0) return "none";
        StringBuilder sb = new StringBuilder();
        for (Object i : roles) sb.append(role(g.getRoleById(String.valueOf(i))));
        return sb.toString();
    }

    private String users(JSONObject o, Guild g) {
        List<Object> users = o.getJSONArray("users").toList();
        if (users.size() == 0) return "none";
        StringBuilder sb = new StringBuilder();
        for (Object i : users) {
            User u = g.getMemberById(String.valueOf(i)).getUser();
            sb.append(u.getName()).append("#").append(u.getDiscriminator());
        }
        return sb.toString();
    }

    private String role(Role r) {
        return "@" + r.getName() + ", id: " + r.getId();
    }

    private String alias() {
        StringBuilder sb = new StringBuilder();
        for (String key : alias.keySet()) sb.append(key).append(" -> ").append(alias.get(key));
        return sb.length() == 0 ? "none" : sb.toString();
    }
}
