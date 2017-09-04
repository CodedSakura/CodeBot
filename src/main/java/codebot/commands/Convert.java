package codebot.commands;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigInteger;

class Convert extends Command {
    Convert() {
        name = "convert";
        info = "Converts units (bases (only between 2 and 36 (inclusive)), length, time, speed, mass, etc.)";
        usage.add("unit from, unit to, value");
        usage.add("\"b\"base from, \"b\"base to, value");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        if (args.length < 3) return send(message.getTextChannel(), id, "ERR: not enough arguments");

        String baseRegex = "b([2-9]|[1-2][0-9]|3[0-6])";
        if (args[0].matches(baseRegex) && args[1].matches(baseRegex)) {
            return send(
                    message.getTextChannel(),
                    id,
                    convert(new Integer(args[0].substring(1)), new Integer(args[1].substring(1)), args[2])
            );
        } else {
            try {
                return send(message.getTextChannel(), id, convert(args[0], args[1], args[2]));
            } catch (IOException e) {
                return send(message.getTextChannel(), id, "ERR: IOException");
            }
        }
    }

    static String convert(int baseFrom, int baseTo, String in) {
        return new BigInteger(in, baseFrom).toString(baseTo).toUpperCase();
    }

    private static String convert(String from, String to, String in) throws IOException {
        Element doc  = Jsoup.connect("https://www.convertunits.com/from/" + in + "+" + from + "/to/" + to).get().body();
        Elements out = doc.select("input[onchange=backward()]");
        if (out.size() != 0) {
            return out.first().attr("value");
        } else {
            if (doc.select("i").size() != 0) {
                return "ERR: incompatible types";
            } else {
                String[] a = doc.select("font[color=red]").first().select("a").first().attr("href").split(" ");
                return convert(a[0], a[2], in);
            }
        }
    }
}
