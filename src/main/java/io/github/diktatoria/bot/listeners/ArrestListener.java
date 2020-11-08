package io.github.diktatoria.bot.listeners;

import io.github.diktatoria.bot.Constants;
import io.github.diktatoria.bot.Utils;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

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
                if (!Utils.hasOneRole(user, new long[]{Constants.DIKTATOR, Constants.REBELL_LEADER}, event.getServer().get())) {
                    event.getChannel().sendMessage(Constants.ERROR_EMBED
                            .setDescription("Nur der große Diktator und der Rebellenanführer können Leute festnehmen lassen!"))
                            .thenAccept((message) -> scheduler.schedule(() -> message.delete(), 30L, TimeUnit.SECONDS));;
                    return;
                }
                List<User> mentions = event.getMessage().getMentionedUsers();
                if (mentions.stream().count() != 1L) {
                    event.getChannel().sendMessage(Constants.ERROR_EMBED
                            .setDescription(user.getMentionTag() + ", bitte inhaftiere immer nur eine Person auf einmal."))
                            .thenAccept((message) -> scheduler.schedule(() -> message.delete(), 30L, TimeUnit.SECONDS));;
                    return;
                }
                User mentioned = mentions.get(0);
                if (Utils.hasOneRole(mentioned, new long[]{Constants.ARRESTED_FROM_DIKTATOR, Constants.ARRESTED_FROM_REBEL_L}, event.getServer().get())) {
                    event.getChannel().sendMessage(Constants.ERROR_EMBED
                            .setDescription(user.getMentionTag() + ", " + mentioned.getDisplayName(event.getServer().get()) + " ist bereits Opfer der Konflikte zwischen den Rebellen und dem Diktator geworden, er langweilt sich schon genug. Du kannst ihn zurzeit nicht gefangen nehmen!"))
                            .thenAccept((message) -> scheduler.schedule(() -> message.delete(), 30L, TimeUnit.SECONDS));
                    ;
                    return;
                }
                if (mentioned.isBot()) {
                    event.getChannel().sendMessage(Constants.ERROR_EMBED
                            .setDescription(user.getMentionTag() + ", Bots sind unantastbar!"))
                            .thenAccept((message) -> scheduler.schedule(() -> message.delete(), 30L, TimeUnit.SECONDS));
                    ;
                    return;
                }
                if (Utils.hasRole(mentioned, Constants.REBELL_LEADER, event.getServer().get()) || Utils.hasRole(mentioned, Constants.DIKTATOR, event.getServer().get())) {
                    event.getChannel().sendMessage(Constants.ERROR_EMBED
                            .setDescription(user.getMentionTag() + ", dachtest du wirklich, dass es so leicht ginge? Einfach deinen Gegenspieler festnehmen zu lassen?"))
                            .thenAccept((message) -> scheduler.schedule(() -> message.delete(), 30L, TimeUnit.SECONDS));
                    return;
                }
                if (mentioned.getId() == user.getId()) {
                    EmbedBuilder builder = Constants.ERROR_EMBED;
                    if (Utils.hasRole(user, Constants.DIKTATOR, event.getServer().get()))
                        builder.setDescription("Du willst dich selber festnehmen? Die Rebellen würden sich sicher freuen!");
                    else
                        builder.setDescription("Du willst dich selber festnehmen? Der Diktator und seine Anhänger würden sich sicher freuen!");
                    event.getChannel().sendMessage(builder)
                            .thenAccept((message) -> scheduler.schedule(() -> message.delete(), 30L, TimeUnit.SECONDS));
                    return;
                }
                if (Utils.hasRole(user, Constants.DIKTATOR, event.getServer().get()))
                    mentioned.addRole(event.getApi().getRoleById(Constants.ARRESTED_FROM_DIKTATOR).get());
                else
                    mentioned.addRole(event.getApi().getRoleById(Constants.ARRESTED_FROM_REBEL_L).get());
                EmbedBuilder builder = Constants.SUCESS_EMBED;
                if (Utils.hasRole(user, Constants.DIKTATOR, event.getServer().get()))
                    builder
                            .setTitle(mentioned.getDisplayName(event.getServer().get()) + " ist von " + user.getDisplayName(event.getServer().get()) + ", dem Großen Diktator einsperren lassen worden.")
                            .setDescription(mentioned.getMentionTag() + ", das Imperium wünscht dir einen schönen, bequemen Aufenthalt im Knast. Viel Spaß!");
                else
                    builder
                            .setTitle(mentioned.getDisplayName(event.getServer().get()) + " wurde von " + user.getDisplayName(event.getServer().get()) + ", dem Rebellenanführer verschleppen lassen worden.")
                            .setDescription(mentioned.getMentionTag() + ", die Rebellen wünschen dir einen schönen, bequemen Aufenthalt in dem modrigen Keller, in dem du eingesperrt worden bist. Viel Spaß!")
                            ;
                mentioned.getPrivateChannel().ifPresent((channel) -> channel.sendMessage(builder));
                event.getChannel().sendMessage(builder);
            });
            scheduler.schedule(() -> event.getMessage().delete(), 30L, TimeUnit.SECONDS);
        }
    }
}
