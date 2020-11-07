package io.github.diktatoria.bot;

import io.github.diktatoria.bot.listeners.ArrestListener;
import io.github.diktatoria.bot.listeners.ConsoleListener;
import io.github.diktatoria.bot.listeners.RuleAcceptListener;
import io.github.diktatoria.bot.listeners.ShutdownListener;
import org.javacord.api.AccountType;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.omg.SendingContext.RunTime;

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
        start(this.api);


    }

    public static void main(String[] args) {
        if (args.length != 2) return;
        DiktatoriaBot bot = new DiktatoriaBot(args[0]);
    }

    public static void start(DiscordApi api){
        api.getTextChannelById(762644745511239693L).get().addMessageCreateListener(new RuleAcceptListener());
        api.getServerById(Constants.SERVER).get().addMessageCreateListener(new ArrestListener());
        TextChannel console = api.getTextChannelById(773246268364816394L).get();
        console.addMessageCreateListener(new ConsoleListener(api, api.getServerById(Constants.SERVER).get()));
        console.addMessageCreateListener(new ShutdownListener());
        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new Utils.ShutdownHook(api));
    }

}
