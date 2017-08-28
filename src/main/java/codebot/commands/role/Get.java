package codebot.commands.role;

import codebot.Command;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import static codebot.Listener.roles;

class Get extends Command {
    Get() {
       name = "get";
       info = "Gets users with selected role";
       usage.add("\"none\"");
       usage.add("role name");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        if (args[0].equals("none")) {
            List<Member> noRole = new ArrayList<>(message.getGuild().getMembers());
            noRole.removeIf(member -> member.getRoles().contains(message.getGuild().getRoleById(roles.getString("bot"))));
            int a = noRole.size();
            noRole.removeIf(member -> member.getRoles().size() > 1);
            noRole.sort(Comparator.comparing(o -> o.getUser().getName().toLowerCase()));
            String[] out = {""};
            out[0] = noRole.size() + " out of " + a + " users don't have a language role:\n";
            noRole.forEach(member -> out[0] += "\t" + member.getUser().getName() + "#" + member.getUser().getDiscriminator() + "\n");
            return send(message.getTextChannel(), id, out[0]);
        } else {
            Role r;
            if (message.getMentionedRoles().size() > 0)
                r = message.getMentionedRoles().get(0);
            else if (message.getGuild().getRolesByName(args[0], true).size() > 0)
                r = message.getGuild().getRolesByName(args[0], true).get(0);
            else return send(message.getTextChannel(), id, "please provide a valid role to sort by!");

            List<Member> hasRole = new ArrayList<>(message.getGuild().getMembersWithRoles(r));
            hasRole.sort(Comparator.comparing(o -> o.getUser().getName().toLowerCase()));
            final String[] out = {""};
            hasRole.forEach(member -> out[0] += member.getUser().getName() + "#" + member.getUser().getDiscriminator() + "\n");
            return send(message.getTextChannel(), id, out[0]);
        }
    }
}
