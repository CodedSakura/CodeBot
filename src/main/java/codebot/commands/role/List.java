package codebot.commands.role;

import codebot.Command;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;

import java.util.ArrayList;
import java.util.Comparator;

import static codebot.Listener.roles;


class List extends Command {
    List() {
        name = "list";
        info = "List available roles";
        usage.add("");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        StringBuilder out = new StringBuilder("```Available roles:\n");
        ArrayList<Role> roles = new ArrayList<>(roles(message.getGuild()));
        roles.sort(Comparator.comparing(role -> role.getName().toLowerCase()));
        for (Role r : roles) out.append("  ").append(r.getName()).append("\n");
        out.append("```");
        return send(message.getTextChannel(), id, out.toString());
    }

    private java.util.List<Role> roles(Guild g) {
        java.util.List<Role> o = new ArrayList<>();
        o.addAll(g.getRoles());
        for (Object s : roles.getJSONArray("blacklist").toList())
            if (o.contains(g.getRoleById(String.valueOf(s))))
                o.remove(g.getRoleById((String) s));

        o.remove(g.getPublicRole());
        return o;
    }
}
