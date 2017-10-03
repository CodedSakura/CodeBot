package codebot.commands.music.sample;

import codebot.Command;

class Play extends Command {
    Play() {
        name = "play";
        info = "Plays a preset audio sample";
        usage.add("sampleName");
    }
}