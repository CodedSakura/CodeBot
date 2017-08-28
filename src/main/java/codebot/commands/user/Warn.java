package codebot.commands.user;

import codebot.Command;
import codebot.FileIO;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.util.HashMap;
import java.util.Map;

class Warn extends Command {
    Warn() {
        name = "warn";
        info = "Gives the user a warning, after 3 warnings user is kicked, after 5 - banned";
        usage.add("user");
        usage.add("user, reason");
        permLevel = 1;
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        User u = getUser(message, args[0]);
        if (u == null) return send(message.getTextChannel(), id, "ERR: user not found!");
        Map<String, String> warnings = getCSV();
        final int strikes = warnings.containsKey(u.getId()) ? new Integer(warnings.get(u.getId())) + 1 : 1;
        u.openPrivateChannel().queue(q -> {
            if (args.length > 1) {
                q.sendMessage("" +
                        "You got warned by " + message.getMember().getEffectiveName() + ", reason:\n" +
                        args[1] + "\nYou now have " + strikes + " warning" + SOrNoS(strikes)
                ).queue();
            } else {
                q.sendMessage("" +
                        "You got warned by " + message.getMember().getEffectiveName() + "\n" +
                        "You now have " + strikes + " warning" + SOrNoS(strikes)
                ).queue();
            }
        });
        warnings.put(u.getId(), strikes + "");
        putCSV(warnings);
        if (strikes == 3) {
            message.getGuild().getController().kick(u.getId(), "3 strikes").queue();
            return send(message.getTextChannel(), id, u.getName() + " was kicked for 3 warnings");
        }
        if (strikes == 5) {
            message.getGuild().getController().ban(u.getId(), 0, "5 strikes").queue();
            return send(message.getTextChannel(), id, u.getName() + " was banned for 5 warnings");
        }
        return send(message.getTextChannel(), id, u.getName() + " has " + strikes + " warning" + SOrNoS(strikes));
    }

    static User getUser(Message message, String s) {
        if (message.getMentionedUsers().size() > 0) return message.getMentionedUsers().get(0);
        else if (message.getGuild().getMembersByEffectiveName(s, true).size() > 0) return message.getGuild().getMembersByEffectiveName(s, true).get(0).getUser();
        else if (message.getGuild().getMemberById(s) != null) return message.getGuild().getMemberById(s).getUser();
        return null;
    }

    static void putCSV(Map<String, String> in) {
        StringBuilder out = new StringBuilder();
        for (String key : in.keySet()) out.append(key).append(",").append(in.get(key)).append("\n");
        FileIO.write("warnings.csv", out.toString().trim(), false);
    }

    static Map<String, String> getCSV() {
        String[][] in = FileIO.CSVParse(FileIO.read("warnings.csv"));
        Map<String, String> out = new HashMap<>();
        for (String[] i : in) out.put(i[0], i[1]);
        return out;
    }
}
