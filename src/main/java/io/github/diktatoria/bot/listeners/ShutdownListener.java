package io.github.diktatoria.bot.listeners;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class ShutdownListener implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessage().getContent().equals(">shutdown")) {
            event.getMessageAuthor().asUser().ifPresent(user -> {
                if (user.getDiscriminatedName().equals("Dr_Dee#2314")) {
                    event.getApi().disconnect();
                    System.exit(1);
                }
            });

        }
    }
}
