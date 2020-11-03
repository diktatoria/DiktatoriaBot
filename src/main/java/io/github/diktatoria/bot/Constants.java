package io.github.diktatoria.bot;

import com.vdurmont.emoji.EmojiParser;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;

public class Constants {
    public static final long ACCEPTED_RULES = 773176626484609067L;
    public static final long ORGA = 762646703978577942L;

    public static final long SERVER = 762644745511239690L;

    public static final EmbedBuilder ERROR_EMBED = new EmbedBuilder()
            .setFooter("Mit " + EmojiParser.parseToUnicode(":heart:") + " entwickelt.")
            .setColor(Color.RED);
    public static final EmbedBuilder SUCESS_EMBED = new EmbedBuilder()
            .setFooter("Mit " + EmojiParser.parseToUnicode(":heart:") + " entwickelt.")
            .setColor(Color.GREEN);
}
