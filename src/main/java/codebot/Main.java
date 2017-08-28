package codebot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;

public class Main {
    public static void main(String[] args) {
        try {
            new JDABuilder(AccountType.BOT)
                    .setToken("token")
                    .addEventListener(new Listener()).buildBlocking();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
