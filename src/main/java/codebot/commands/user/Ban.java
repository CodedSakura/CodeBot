package codebot.commands.user;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import static codebot.commands.user.Warn.getUser;

class Ban extends Command {
    Ban() {
        name = "ban";
        info = "Bans people";
        usage.add("user");
        usage.add("user, reason");
        permLevel = 1;
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        User u = getUser(message, args[0]);
        if (u == null) return send(message.getTextChannel(), id, "ERR: user not found!");
        u.openPrivateChannel().queue(q -> {
            if (args.length > 1) {
                q.sendMessage("" +
                        "You got banned by " + message.getMember().getEffectiveName() + ", reason:\n" +
                        args[1] + "\n"
                ).queue();
            } else {
                q.sendMessage("" +
                        "You got banned by " + message.getMember().getEffectiveName()
                ).queue();
            }
        });
        if (args.length > 1) message.getGuild().getController().ban(u, 0, args[1]).queue();
        else message.getGuild().getController().ban(u, 0).queue();
        return send(message.getTextChannel(), id, u.getName() + " was banned");
    }
}
