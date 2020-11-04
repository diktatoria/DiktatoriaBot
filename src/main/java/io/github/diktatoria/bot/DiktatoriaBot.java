package io.github.diktatoria.bot;

import io.github.diktatoria.bot.listeners.ConsoleListener;
import io.github.diktatoria.bot.listeners.RuleAcceptListener;
import io.github.diktatoria.bot.listeners.ShutdownListener;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.concurrent.atomic.AtomicBoolean;

public class DiktatoriaBot {
    private DiscordApi api;

    public DiktatoriaBot(String token) {
        this.api = this.api = new DiscordApiBuilder()
                .setToken(token)
                .login().join();

        this.api.getTextChannelById(762644745511239693L).get().addMessageCreateListener(new RuleAcceptListener());
        this.api.getTextChannelById(773246268364816394L).get().addMessageCreateListener(new ConsoleListener(api, api.getServerById(Constants.SERVER).get()));
        this.api.getTextChannelById(773246268364816394L).get().addMessageCreateListener(new ShutdownListener());
    }

    public static void main(String[] args) {
        if (args.length != 1) return;
        DiktatoriaBot bot = new DiktatoriaBot(args[0]);
    }

}
