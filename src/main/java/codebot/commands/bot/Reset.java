package codebot.commands.bot;

import codebot.Command;
import codebot.FileIO;
import net.dv8tion.jda.core.entities.Message;
import org.json.JSONObject;

import java.util.HashMap;

import static codebot.Listener.*;

class Reset extends Command {
    Reset() {
        name = "reset";
        info = "Resets the bot";
        usage.add("");
        permLevel = 1;
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        latest = new HashMap<>();
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
        main = new codebot.commands.Main();

        alias.clear();
        for (String i : cfgAlias.keySet())
            alias.put(i, cfgAlias.getString(i));

        System.out.println("Using prefix: " + prefix);

        return send(message.getTextChannel(), id, "success");
    }
}
