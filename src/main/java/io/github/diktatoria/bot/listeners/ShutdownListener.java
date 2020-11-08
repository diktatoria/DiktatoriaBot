package io.github.diktatoria.bot.listeners;

import io.github.diktatoria.bot.Constants;
import io.github.diktatoria.bot.Utils;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ShutdownListener implements MessageCreateListener {
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(3);

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessage().getContent().equals(">shutdown")) {
            event.getMessageAuthor().asUser().ifPresent(user -> {
                if (Utils.hasRole(user, Constants.BOT_CONTROL, event.getServer().get())) {
                    event.getChannel().sendMessage(Constants.SUCESS_EMBED
                            .setTitle("Bot wird beendet")
                            .setDescription("Dieser Bot wurde von " + user.getMentionTag() + "beendet.")).thenAccept(t -> {
                        scheduler.schedule(() -> {
                            t.delete();
                            event.getApi().disconnect();
                            System.exit(-1);

                        }, 5L, TimeUnit.SECONDS);
                    });
                    System.out.println("Bot wurde von " + user.getDiscriminatedName() + " gestoppt.");
                } else {
                    event.getChannel().sendMessage(Constants.noPerms(user));
                }
            });
            scheduler.schedule(()->event.getMessage().delete(), 30, TimeUnit.SECONDS);

        }
    }
}
