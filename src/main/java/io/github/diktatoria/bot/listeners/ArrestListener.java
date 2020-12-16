package io.github.diktatoria.bot.listeners;

import io.github.diktatoria.bot.Constants;
import io.github.diktatoria.bot.Utils;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.util.logging.ExceptionLogger;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ArrestListener implements MessageCreateListener {
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(3);

    private void println(Object o) {
        System.out.println(o);
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessage().getContent().startsWith(">arrest") && event.getMessage().getMentionedUsers().stream().count() != 0) {
            event.getMessageAuthor().asUser().ifPresent(user -> {
                if (!Utils.hasOneRole(user, new long[]{Constants.DIKTATOR}, event.getServer().get())) {
                    user.openPrivateChannel().thenAccept(privateChannel -> privateChannel.sendMessage(Constants.ERROR_EMBED
                            .setDescription("Nur der große Diktator kann Leute festnehmen lassen!"))
                    );
                    return;
                }
                List<User> mentions = event.getMessage().getMentionedUsers();
                if (mentions.stream().count() != 1L) {
                    user.openPrivateChannel().thenAccept(privateChannel -> privateChannel.sendMessage(Constants.ERROR_EMBED
                            .setDescription(user.getMentionTag() + ", bitte inhaftiere immer nur eine Person auf einmal."))
                    );
                    return;
                }
                User mentioned = mentions.get(0);
                if (Utils.hasOneRole(mentioned, new long[]{Constants.ARRESTED_FROM_DIKTATOR, Constants.ARRESTED_FROM_REBEL_L}, event.getServer().get())) {
                    user.openPrivateChannel().thenAccept(privateChannel -> privateChannel.sendMessage(Constants.ERROR_EMBED
                            .setDescription(user.getMentionTag() + ", " + mentioned.getDisplayName(event.getServer().get()) + " ist bereits Opfer der Konflikte zwischen den Rebellen und dem Diktator geworden, er langweilt sich schon genug. Du kannst ihn zurzeit nicht gefangen nehmen!"))
                    );

                    return;
                }
                if (mentioned.isBot()) {
                    user.openPrivateChannel().thenAccept(privateChannel -> {
                        privateChannel.sendMessage(Constants.ERROR_EMBED
                                .setDescription(user.getMentionTag() + ", Bots sind unantastbar!"));
                    });
                    return;
                }
                if (Utils.hasRole(mentioned, Constants.REBELL_LEADER, event.getServer().get()) || Utils.hasRole(mentioned, Constants.DIKTATOR, event.getServer().get())) {
                    user.openPrivateChannel().thenAccept(privateChannel -> privateChannel.sendMessage(Constants.ERROR_EMBED
                            .setDescription(user.getMentionTag() + ", dachtest du wirklich, dass es so leicht ginge? Einfach deinen Gegenspieler festnehmen zu lassen?")));
                    return;
                }
                if (mentioned.getId() == user.getId()) {
                    EmbedBuilder builder = Constants.ERROR_EMBED.setDescription("Du willst dich selber festnehmen? Die Rebellen würden sich sicher freuen!");
                    user.openPrivateChannel().thenAccept(privateChannel -> privateChannel.sendMessage(builder));
                    return;
                }
                if (Utils.hasRole(user, Constants.DIKTATOR, event.getServer().get()))
                    mentioned.addRole(event.getApi().getRoleById(Constants.ARRESTED_FROM_DIKTATOR).get());
                EmbedBuilder builder = Constants.SUCESS_EMBED
                        .setTitle(mentioned.getDisplayName(event.getServer().get()) + " wurde inhaftiert!")
                        .setDescription(mentioned.getDisplayName(event.getServer().get()) + " wurde von " + user.getDisplayName(event.getServer().get()) + ", dem Großem Diktator dich ordnungsremäß inhaftieren lassen und wünscht dir einen angenehmen Aufenthalt im Knast. Viel Spaß!");
                mentioned.openPrivateChannel().thenAccept(privateChannel -> privateChannel.sendMessage(builder));
                event.getChannel().sendMessage(builder)
                ;
            });
            scheduler.schedule(() -> event.getMessage().delete(), 30L, TimeUnit.SECONDS);
            event.getMessage().delete();
        } else if (event.getMessage().getContent().startsWith(">arrest")) {
            event.getMessageAuthor().asUser().ifPresent(user -> {
                user.openPrivateChannel().thenAccept(privateChannel ->
                        privateChannel.sendMessage(Constants.INFO_EMBED
                                .setDescription("Syntax: >arrest <Spieler>")
                                .addField("Benötigter Rang", "Der Große Diktator"))
                );
            });
            event.getMessage().delete();
        }
    }
}
