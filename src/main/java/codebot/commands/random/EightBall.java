package codebot.commands.random;

import codebot.Command;
import net.dv8tion.jda.core.entities.Message;

import java.util.Random;

class EightBall extends Command {
    EightBall() {
        name = "8ball";
        info = "Predicts the future (don't drink the blue liquid)";
        usage.add("");
        usage.add("question");
    }

    @Override
    public String run(Message message, String[] args, byte perms, String id) {
        String[] answers = {
                "It is certain",
                "It is decidedly so",
                "Without a doubt",
                "Yes definitely",
                "You may rely on it",
                "As I see it, yes",
                "Most likely",
                "Outlook good",
                "Yes",
                "Signs point to yes",
                "Reply hazy try again",
                "Ask again later",
                "Better not tell you now",
                "Cannot predict now",
                "Concentrate and ask again",
                "Don't count on it",
                "My reply is no",
                "My sources say no",
                "Outlook not so good",
                "Very doubtful"
        };
        return send(message.getTextChannel(), id, answers[new Random().nextInt(answers.length)]);
    }
}
