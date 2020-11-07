package io.github.diktatoria.bot;

import com.vdurmont.emoji.EmojiParser;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.awt.*;

public class Constants {

    //ROLES
    public static final long ACCEPTED_RULES = 773176626484609067L;
    public static final long ORGA = 762646703978577942L;
    public static final long BOT_CONTROL = 773612171891441666L;
    public static final long DIKTATOR = 762646433744158731L;
    public static final long ARRESTED = 773623127640899655L;
    public static final long REBELL_LEADER = 762647132192374804L;

    //SERVERS
    public static final long SERVER = 762644745511239690L;

    //CHANNELS
    public static final long CONSOLE = 773246268364816394L;

    //REACTIONS
    public static final long ARREST = 773608489649831937L;
    public static final long SETFREE = 773622690078654464L;

    public static final EmbedBuilder ERROR_EMBED = new EmbedBuilder()
            .setFooter("Mit " + EmojiParser.parseToUnicode(":heart:") + " entwickelt.")
            .setTitle("Fehler")
            .setColor(Color.RED);
    public static final EmbedBuilder SUCESS_EMBED = new EmbedBuilder()
            .setFooter("Mit " + EmojiParser.parseToUnicode(":heart:") + " entwickelt.")
            .setColor(Color.GREEN);

    public static EmbedBuilder noPerms(User user){
        return ERROR_EMBED
                .setTitle("Keine Berechtigung")
                .setDescription(user.getMentionTag() + ", du hast nicht genügend Berechtigungen für diese Aktion. Wenn du denkst, das ist ein Missverständniss, melde dich bitte beim Orga-Team.");

    }

}
