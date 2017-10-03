package codebot.commands.music.sample;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;

class List extends Command {
    List() {
        name = "list";
        info = "Lists all the samples";
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        return super.run(message, args, perms, id);
    }

    static String[] list() {
        return new String[] {};
    }
}