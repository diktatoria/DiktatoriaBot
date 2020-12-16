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

public class ReleaseListener implements MessageCreateListener {
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(3);

    private void println(Object o) {
        System.out.println(o);
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessage().getContent().startsWith(">release") && event.getMessage().getMentionedUsers().stream().count() != 0) {
            event.getMessageAuthor().asUser().ifPresent(user -> {
                if (!Utils.hasOneRole(user, new long[]{Constants.DIKTATOR, Constants.REBELL_LEADER}, event.getServer().get())) {
                    event.getChannel().sendMessage(Constants.ERROR_EMBED
                            .setDescription("Nur der große Diktator und der Rebellenanführer können Leute freilassen!"))
                            .thenAccept((message) -> scheduler.schedule(() -> message.delete(), 30L, TimeUnit.SECONDS));;
                    return;
                }
                List<User> mentions = event.getMessage().getMentionedUsers();
                if (mentions.stream().count() != 1L) {
                    event.getChannel().sendMessage(Constants.ERROR_EMBED
                            .setDescription(user.getMentionTag() + ", bitte lasse immer nur eine Person auf einmal frei."))
                            .thenAccept((message) -> scheduler.schedule(() -> message.delete(), 30L, TimeUnit.SECONDS));;
                    return;
                }
                User mentioned = mentions.get(0);
                if (!Utils.hasOneRole(mentioned, new long[]{Constants.ARRESTED_FROM_DIKTATOR, Constants.ARRESTED_FROM_REBEL_L}, event.getServer().get())) {
                    event.getChannel().sendMessage(Constants.ERROR_EMBED
                            .setDescription(user.getMentionTag() + ", " + mentioned.getDisplayName(event.getServer().get()) + " ist gar kein Gefangener! Bitte lasse jemanden anderen frei!"))
                            .thenAccept((message) -> scheduler.schedule(() -> message.delete(), 30L, TimeUnit.SECONDS));
                    ;
                    return;
                }

                if (Utils.hasRole(mentioned, Constants.REBELL_LEADER, event.getServer().get()) && Utils.hasRole(mentioned, Constants.ARRESTED_FROM_DIKTATOR, event.getServer().get())) {
                    event.getChannel().sendMessage(Constants.ERROR_EMBED
                            .setDescription(user.getMentionTag() + ", " + mentioned.getDisplayName(event.getServer().get())) + "wurde nicht von den Rebellen verschleppt, sondern vom Diktator! Du kannst nur deine eigenen Gefangenen freilassen!")
                            .thenAccept((message) -> scheduler.schedule(() -> message.delete(), 30L, TimeUnit.SECONDS));
                    return;
                }
                if (Utils.hasRole(mentioned, Constants.DIKTATOR, event.getServer().get()) && Utils.hasRole(mentioned, Constants.ARRESTED_FROM_REBEL_L, event.getServer().get())) {
                    event.getChannel().sendMessage(Constants.ERROR_EMBED
                            .setDescription(user.getMentionTag() + ", " + mentioned.getDisplayName(event.getServer().get())) + "wurde von den Rebellen verschleppt, er wurde nicht von dir festnehmen lassen! Du kannst nur deine eigenen Gefangenen freilassen!")
                            .thenAccept((message) -> scheduler.schedule(() -> message.delete(), 30L, TimeUnit.SECONDS));
                    return;
                }

                if (Utils.hasRole(mentioned, Constants.ARRESTED_FROM_DIKTATOR, event.getServer().get())) {
                    mentioned.removeRole(event.getServer().get().getRoleById(Constants.ARRESTED_FROM_DIKTATOR).get());
                }
                else {
                    event.getServer().get().getRoleById(Constants.ARRESTED_FROM_REBEL_L).get().removeUser(user).thenAccept((voit)->{
                        println(event.getServer().get().getRoleById(Constants.ARRESTED_FROM_REBEL_L).get().getUsers());
                    }).exceptionally(ExceptionLogger.get());
                    mentioned.removeRole(event.getServer().get().getRoleById(Constants.ARRESTED_FROM_REBEL_L).get());
                }
                EmbedBuilder builder = Constants.SUCESS_EMBED;
                if (Utils.hasRole(user, Constants.DIKTATOR, event.getServer().get()))
                    builder
                            .setTitle(mentioned.getDisplayName(event.getServer().get()) + " ist von " + user.getDisplayName(event.getServer().get()) + ", dem Großen Diktator freigelassen worden.")
                            .setDescription(mentioned.getMentionTag() + ", das Imperium hat dich soeben freigelassen. Viel Spaß (und beeil dich, nicht das du noch einmal eingesperrt wirst, weil ihr nicht schnell genug eine Revolution angezettelt habt!");
                else
                    builder
                            .setTitle(mentioned.getDisplayName(event.getServer().get()) + " wurde von " + user.getDisplayName(event.getServer().get()) + ", dem Rebellenanführer freigelassen worden.")
                            .setDescription("Herzlichen Glückwunsch," + mentioned.getMentionTag() + ", die Rebellen haben dich soeben freigelassen! Viel Spaß in der Freiheit der Diktatur!")
                            ;
                mentioned.getPrivateChannel().ifPresent((channel) -> channel.sendMessage(builder));
                event.getChannel().sendMessage(builder);
            });
            event.getMessage().delete();
        }
    }
}
