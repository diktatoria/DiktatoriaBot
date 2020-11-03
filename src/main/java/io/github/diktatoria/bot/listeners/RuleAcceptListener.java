package io.github.diktatoria.bot.listeners;

import io.github.diktatoria.bot.Constants;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.concurrent.atomic.AtomicBoolean;

public class RuleAcceptListener implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessage().getContent().equals(">accept")) {
            event.getMessageAuthor().asUser().ifPresent(user -> {
                AtomicBoolean b = new AtomicBoolean(false);
                user.getRoles(event.getServer().get()).forEach(role -> {
                    if(role.getId() == Constants.ACCEPTED_RULES)
                        b.set(true);
                });
                if(!b.get()){
                    user.addRole(event.getApi().getRoleById(Constants.ACCEPTED_RULES).get(), "Hat die Regeln aktzeptiert");
                    event.getChannel().sendMessage(Constants.SUCESS_EMBED
                    .setTitle("Regeln erfolgreich aktzeptiert!")
                    .setDescription(user.getMentionTag() + ", du bist nun ein vollwertiges Mitglied von Diktatoria und kannst dir nun in <#769228580357668874> Rollen zuweisen."));
                }else {
                    event.getChannel().sendMessage(Constants.ERROR_EMBED
                    .setTitle("Fehler")
                    .setDescription("Du hast die Regeln bereits aktzeptiert!"));
                }
            });

        }


    }
}
