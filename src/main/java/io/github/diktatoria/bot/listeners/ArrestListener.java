package io.github.diktatoria.bot.listeners;

import io.github.diktatoria.bot.Constants;
import io.github.diktatoria.bot.Utils;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.List;

public class ArrestListener implements MessageCreateListener {

    private void println(Object o) {
        System.out.println(o);
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessage().getContent().startsWith(">arrest") && event.getMessage().getMentionedUsers().stream().count() != 0) {
            event.getMessageAuthor().asUser().ifPresent(user -> {
                if (!Utils.hasOneRole(user, new long[]{Constants.DIKTATOR, Constants.REBELL_LEADER}, event.getServer().get())) {
                    event.getChannel().sendMessage(Constants.ERROR_EMBED
                            .setDescription("Nur der große Diktator und der Rebellenanführer können Leute festnehmen lassen!"));
                    return;
                }
                List<User> mentions = event.getMessage().getMentionedUsers();
                if (mentions.stream().count() != 1L) {
                    event.getChannel().sendMessage(Constants.ERROR_EMBED
                            .setDescription(user.getMentionTag() + ", bitte inhaftiere immer nur eine Person auf einmal."));
                    return;
                }
                User mentioned = mentions.get(0);
                if(mentioned.isBot()){
                    event.getChannel().sendMessage(Constants.ERROR_EMBED
                    .setDescription(user.getMentionTag() + ", Bots sind unantastbar!"));
                    return;
                }
                if(Utils.hasRole(mentioned, Constants.REBELL_LEADER, event.getServer().get()) || Utils.hasRole(mentioned, Constants.DIKTATOR, event.getServer().get())){
                    event.getChannel().sendMessage(Constants.ERROR_EMBED
                    .setDescription(user.getMentionTag() + ", dachtest du wirklich, dass es so leicht gänge? Einfach deinen Gegenspieler festnehmen zu lassen?"));
                }
                if (mentioned.getId() == user.getId()) {
                    EmbedBuilder builder = Constants.ERROR_EMBED;
                    if (Utils.hasRole(user, Constants.DIKTATOR, event.getServer().get()))
                        builder.setDescription("Du willst dich selber festnehmen? Die Rebellen würden sich sicher freuen!");
                    else
                        builder.setDescription("Du willst dich selber festnehmen? Der Diktator und seine Anhänger würden sich sicher freuen!");
                    event.getChannel().sendMessage(builder);
                    return;
                }
                mentioned.addRole(event.getApi().getRoleById(Constants.ARRESTED).get());
                EmbedBuilder builder = Constants.SUCESS_EMBED;
                if (Utils.hasRole(user, Constants.DIKTATOR, event.getServer().get()))
                    builder
                            .setTitle(mentioned.getDisplayName(event.getServer().get()) + " ist von " + user.getDisplayName(event.getServer().get()) + ", dem Großen Diktator einsperren gelassen worden.")
                            .setDescription(mentioned.getMentionTag() + ", das Imperium wünscht dir einen schönen, bequemen Aufenthalt im Knast. Viel Spaß!");
                else
                    builder
                        .setTitle(mentioned.getDisplayName(event.getServer().get()) + " wurde von " + user.getDisplayName(event.getServer().get()) + ", dem Rebellenanführer verschleppen gelassen worden.")
                        .setDescription(mentioned.getMentionTag() + ", die Rebellen wünschen dir einen schönen, bequemen Aufenthalt in dem modrigen Keller, in dem du eingesperrt worden bist. Viel Spaß!")
                ;
                event.getChannel().sendMessage(builder);
            });

        }
    }
}
