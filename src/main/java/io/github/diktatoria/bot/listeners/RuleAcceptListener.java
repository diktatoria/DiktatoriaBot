package io.github.diktatoria.bot.listeners;

import io.github.diktatoria.bot.Constants;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class RuleAcceptListener implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if (event.getMessage().getContent().equals(">accept")) {
            event.getMessageAuthor().asUser().ifPresent(user -> {
                for(Role r : user.getRoles(event.getApi().getServerById(Constants.SERVER).get())){
                    if(r.getId() != Constants.ACCEPTED_RULES){
                        user.addRole(event.getApi().getRoleById(Constants.ACCEPTED_RULES).get());
                        break;
                    }

                }
            });

        }


    }
}
