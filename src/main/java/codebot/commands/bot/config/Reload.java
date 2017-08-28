package codebot.commands.bot.config;

import codebot.FileIO;
import codebot.Command;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;
import org.json.JSONObject;

import static codebot.Listener.*;

class Reload extends Command {
    Reload() {
        name = "reload";
        info = "Reloads JSON entries";
        usage.add("");
        permLevel = 1;
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        config = new JSONObject(FileIO.read("config.json"));
        channels = config.getJSONObject("channels");
        roles = config.getJSONObject("roles");
        codebot.Listener.perms = config.getJSONObject("perms");
        ignored = codebot.Listener.perms.getJSONObject("ignored");
        lvl1 = codebot.Listener.perms.getJSONObject("level 1");
        lvl2 = codebot.Listener.perms.getJSONObject("level 2");
        cfgAlias = config.getJSONObject("alias");
        prefix = config.getString("prefix");
        limit = config.getInt("limit");

        alias.clear();
        for (String i : cfgAlias.keySet())
            alias.put(i, cfgAlias.getString(i));

        System.out.println("Using prefix: " + prefix);

        message.getJDA().getPresence().setGame(Game.of(config.getString("game")));

        return send(message.getTextChannel(), id, "success");
    }
}
