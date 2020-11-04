package io.github.diktatoria.bot.listeners;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.Optional;

public class ConsoleListener implements MessageCreateListener {
    private DiscordApi api;
    private Server server;

    public ConsoleListener(DiscordApi api, Server server) {
        this.api = api;
        this.server = server;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

    }
}
