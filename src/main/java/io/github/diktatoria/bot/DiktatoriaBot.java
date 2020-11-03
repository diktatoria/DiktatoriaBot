package io.github.diktatoria.bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.List;

public class DiktatoriaBot {
    private DiscordApi api;

    public static final long SERVER = 762644745511239690L;

    public DiktatoriaBot(String token) {
        this.api = this.api = new DiscordApiBuilder()
                .setToken(token)
                .login().join();
        this.api.getTextChannelById(762644745511239693L).ifPresent(channel -> {
            channel.addMessageCreateListener(new MessageCreateListener() {
                @Override
                public void onMessageCreate(MessageCreateEvent event) {
                    if (event.getMessage().getContent().equals(">accept")) {
                        event.getMessageAuthor().asUser().ifPresent(user -> {
                            for(Role r : user.getRoles(api.getServerById(SERVER).get())){
                                if(r.getId() != ROLES.ACCEPTED_RULES){
                                    user.addRole(api.getRoleById(ROLES.ACCEPTED_RULES).get());
                                    break;
                                }

                            }
                        });

                    }


                }
            });
        });
    }

    public static void main(String[] args) {
        if (args.length != 1) return;
        DiktatoriaBot bot = new DiktatoriaBot(args[0]);
    }

}
