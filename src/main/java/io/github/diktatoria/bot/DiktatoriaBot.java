package io.github.diktatoria.bot;

import io.github.diktatoria.bot.listeners.RuleAcceptListener;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class DiktatoriaBot {
    private DiscordApi api;

    public DiktatoriaBot(String token) {
        this.api = this.api = new DiscordApiBuilder()
                .setToken(token)
                .login().join();

        this.api.getTextChannelById(762644745511239693L).get().addMessageCreateListener(new RuleAcceptListener());

    }

    public static void main(String[] args) {
        if (args.length != 1) return;
        DiktatoriaBot bot = new DiktatoriaBot(args[0]);
    }

}
