package codebot.commands.music;

import codebot.Command;
import codebot.commands.music.handle.Handler;
import net.dv8tion.jda.core.entities.Guild;

public class Main extends Command {
    static Handler handler;

    public Main(Guild guild) {
        name = "music";
        info = "Plays nyan cat in a voice channel";

        children.add(new Connect());
        children.add(new Current());
        children.add(new Disconnect());
        children.add(new Pause());
        children.add(new Play());
        children.add(new Queue());
        children.add(new Shuffle());
        children.add(new Skip());

        handler = new Handler(guild);
    }

    static String time(long ms) {
        int sec = (int) Math.floor(ms / 1000);
        int min = (int) Math.floor(sec / 60);
        int hour = (int) Math.floor(min / 60);
        String s = String.valueOf(sec % 60);
        s = sec < 10 ? "0" : "" + s;
        String m = String.valueOf(min % 60);
        if (hour > 0) {
            m = min < 10 ? "0" : "" + m;
            return String.valueOf(hour) + ":" + m + ":" + s;
        }
        return m + ":" + s;
    }
}
