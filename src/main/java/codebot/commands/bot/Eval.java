package codebot.commands.bot;

import codebot.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

class Eval extends Command {
    Eval() {
        name = "eval";
        info = "Evaluates code";
        usage.add("");
        permLevel = 2;
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        ScriptEngine se = new ScriptEngineManager().getEngineByName("Nashorn");
        se.put("bot", message.getJDA());
        se.put("self", message.getAuthor());
        se.put("guild", message.getGuild());
        se.put("member", message.getMember());
        se.put("channel", message.getChannel());
        se.put("message", message);
        se.put("MessageBuilder", new MessageBuilder());
        se.put("EmbedBuilder", new EmbedBuilder());
        se.put("help", "this eval command has 'event', 'bot', 'self', 'guild', 'member', 'channel', 'message', 'MessageBuilder', 'EmbedBuilder'");
        String text = args[0];
        if (text.startsWith("```")) {
            text = text.substring(3, text.length() - 3);
            if (text.startsWith("java")) text = text.substring(4);
            else if (text.startsWith("js")) text = text.substring(2);
        }
        if (text.startsWith("`")) text = text.substring(1, text.length() - 1);

        try {
            return send(message.getTextChannel(), id, "Evaluated successfully:```js\n" + se.eval(text) + " ```");
        } catch (Exception e) {
            return send(message.getTextChannel(), id, "An exception was thrown:```\n" + e + " ```");
        }
    }
}
