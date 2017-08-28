package codebot.commands.role;

import codebot.Command;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;

import static codebot.Listener.roles;

class Set extends Command {
    Set() {
        name = "set";
        info = "Gives the user selected roles";
        usage.add("role");
        usage.add("role, role");
        usage.add("role... roles");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        if (args.length == 0) return null;
        List<Role> toRM = new ArrayList<>();
        List<Role> toADD = new ArrayList<>();
        for (String arg : args) {
            if (message.getGuild().getRolesByName(arg, true).size() > 0) {
                toRM.addAll(message.getGuild().getRolesByName(arg, true));
            } else {
                return send(message.getTextChannel(), id, "the role \"" + arg + "\" does not exist");
            }
        }
        toRM.removeIf(role -> !roles(message.getGuild()).contains(role));
        toADD.addAll(toRM);
        toRM.removeIf(role -> !message.getMember().getRoles().contains(role));
        toADD.removeIf(role -> message.getMember().getRoles().contains(role));
        toRM = new ArrayList<>(new LinkedHashSet<>(toRM));
        toADD = new ArrayList<>(new LinkedHashSet<>(toADD));
        toRM.sort(Comparator.comparing(role -> role.getName().toLowerCase()));
        toADD.sort(Comparator.comparing(role -> role.getName().toLowerCase()));
        message.getGuild().getController().modifyMemberRoles(message.getMember(), toADD, toRM).queue();
        final String[] out = {"", ""};
        toADD.forEach(role -> out[0] += "`" + role.getName() + "`, ");
        toRM.forEach(role -> out[1] += "`" + role.getName() + "`, ");
        if (out[0].length() > 2) out[0] = out[0].substring(0, out[0].length() - 2);
        if (out[1].length() > 2) out[1] = out[1].substring(0, out[1].length() - 2);
        return send(message.getTextChannel(), id, "" +
                (toADD.size() > 0 ? "You now have the role" + SOrNoS(toADD.size()) + ": " + out[0] + "\n" : "") +
                (toRM.size() > 0 ? "You no longer have the role" + SOrNoS(toRM.size()) + ": " + out[1] : "")
        );
    }

    private List<Role> roles(Guild g) {
        List<Role> o = new ArrayList<>();
        o.addAll(g.getRoles());
        for (Object s : roles.getJSONArray("blacklist").toList())
            if (o.contains(g.getRoleById(String.valueOf(s))))
                o.remove(g.getRoleById((String) s));

        o.remove(g.getPublicRole());
        return o;
    }
    public String SOrNoS(int num) {
        return (num % 10 == 1 && num % 100 != 11) ? "" : "s";
    }
}
