package codebot.commands;

import codebot.Command;
import codebot.FileIO;
import net.dv8tion.jda.core.entities.Message;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

class Time extends Command {
    Time() {
        name = "time";
        info = "Times tell... no, wait... tells time";
        usage.add("");
        usage.add("timezone abbreviation");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("EEEE MMMM dd, HH:mm:ss");
        if (args[0].equals("")) {
            ZonedDateTime z = ZonedDateTime.now(ZoneOffset.UTC);
            return send(message.getTextChannel(), id, "Coordinated Universal Time (UTC):\n" + z.format(f));
        } else {
            String[] zone = getZone(args[0]);
            if (zone != null) {
                ZonedDateTime z = ZonedDateTime.now(ZoneOffset.ofTotalSeconds(new Integer(zone[4])));
                return send(message.getTextChannel(), id, zone[1] + " (" + zone[2] + "):\n" + z.format(f));
            } else {
                return send(message.getTextChannel(), id, "ERR: timezone not found");
            }
        }
    }

    static String[] getZone(String abbr) {
        String[][] csv = FileIO.CSVParse(FileIO.read("timezones.csv"));
        for (String[] i : csv) if (i[0].equals(abbr)) return i;
        return null;
    }
}
