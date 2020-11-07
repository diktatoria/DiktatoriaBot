package io.github.diktatoria.bot.listeners;

import io.github.diktatoria.bot.Constants;
import io.github.diktatoria.bot.Utils;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class ShutdownListener implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessage().getContent().equals(">shutdown")) {
            event.getMessageAuthor().asUser().ifPresent(user -> {
                if (Utils.hasRole(user, Constants.BOT_CONTROL, event.getServer().get())) {
                    event.getChannel().sendMessage(Constants.SUCESS_EMBED
                            .setTitle("Bot wird beendet")
                            .setDescription("Dieser Bot wurde von " + user.getMentionTag() + "beendet.")).thenAccept(t -> {
                        event.getApi().disconnect();
                        System.exit(1);
                    });
                    System.out.println("Bot wurde von " + user.getDiscriminatedName() + " gestoppt.");
                } else {
                    event.getChannel().sendMessage(Constants.noPerms(user));
                }
            });

        }
    }
}
