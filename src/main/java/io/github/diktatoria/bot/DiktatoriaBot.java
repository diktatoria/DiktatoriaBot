package io.github.diktatoria.bot;

import io.github.diktatoria.bot.listeners.*;
import org.javacord.api.AccountType;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.UserStatus;

public class DiktatoriaBot {
    public DiscordApi api;

    public String token;

    public DiktatoriaBot(String token) {
        this.token = token;
        this.api = this.api = new DiscordApiBuilder()
                .setAccountType(AccountType.BOT)
                .setAllIntents()
                .setToken(token)
                .login().join();
        api.updateActivity(ActivityType.WATCHING, "Bereitet Dees Weltherrschaft vor...");
        start(this.api);


    }

    public static void main(String[] args) {
        if(System.getenv("BOT_TOKEN") == null) return;
        DiktatoriaBot bot = new DiktatoriaBot(System.getenv("BOT_TOKEN"));
    }

    public static void start(DiscordApi api){

        api.getTextChannelById(Constants.RULES).get().addMessageCreateListener(new RuleAcceptListener());
        api.getServerById(Constants.SERVER).get().addMessageCreateListener(new AbductListener());
        api.getServerById(Constants.SERVER).get().addMessageCreateListener(new ReleaseListener());
        api.getServerById(Constants.SERVER).get().addMessageCreateListener(new ArrestListener());
        TextChannel console = api.getTextChannelById(773246268364816394L).get();
        console.addMessageCreateListener(new ConsoleListener(api, api.getServerById(Constants.SERVER).get()));
        console.addMessageCreateListener(new ShutdownListener());
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new Utils.ShutdownHook(api));

    }

}
