package codebot.commands;

import codebot.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static codebot.Listener.config;

class Latex extends Command {
    Latex() {
        name = "latex";
        info = "Generates a LaTeX image from a string";
        usage.add("latex code");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        TextChannel c = message.getTextChannel();
        try {
            BufferedImage image = ImageIO.read(new URL(latex(args[0])));
            int[] dims = {image.getWidth(), image.getHeight()};
            EmbedBuilder eb = new EmbedBuilder();
            eb.setImage(latex(args[0]));
            if ((dims[0] == 127 && dims[1] == 24) || (dims[0] == 1 && dims[1] == 1)) return send(c, id, "ERR: problem with LaTeX syntax");
            else return send(c, id, eb.build());
        } catch (IOException e) {
            return send(c, id, "ERR: problem with LaTeX syntax");
        }
    }

    private String latex(String e) {
        return config.getString("latex") + e.replaceAll(" ", "&space;");
    }
}
