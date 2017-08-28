package codebot.commands.user;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.util.Map;

class Pardon extends Command {
    Pardon() {
        name = "pardon";
        info = "Removes a warning from a user";
        usage.add("user");
        permLevel = 1;
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        User u = Warn.getUser(message, args[0]);
        if (u == null) return send(message.getTextChannel(), id, "ERR: user not found!");
        Map<String, String> warnings = Warn.getCSV();

        if (!warnings.containsKey(u.getId()) || warnings.get(u.getId()).equals("0"))
            return send(message.getTextChannel(), id, "ERR: user doesn't have any warnings");

        final int strikes = new Integer(warnings.get(u.getId())) - 1;
        u.openPrivateChannel().queue(q -> {
            q.sendMessage("" +
                    "You got pardoned by " + message.getMember().getEffectiveName() + "\n" +
                    "You now have " + strikes + " warning" + SOrNoS(strikes)
            ).queue();
        });
        warnings.put(u.getId(), String.valueOf(strikes));
        Warn.putCSV(warnings);
        return send(message.getTextChannel(), id, u.getName() + " has " + strikes + " warning" + SOrNoS(strikes));
    }
}
