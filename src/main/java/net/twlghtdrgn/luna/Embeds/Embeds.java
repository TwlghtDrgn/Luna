package net.twlghtdrgn.luna.Embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class Embeds {
    EmbedBuilder embedBuilder = new EmbedBuilder();

    /**
     * Embed builder
     * @return embed with title, description and color
     */
    public MessageEmbed setEmbed(String title, String description, int color) {
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setColor(new Color(color));
        return embedBuilder.build();
    }

    /**
     * Embed builder
     * @return embed with title, description, color and thumbnail
     */
    public MessageEmbed setEmbed(String title, String description, int color, String image) {
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(description);
        embedBuilder.setColor(new Color(color));
        embedBuilder.setThumbnail(image);
        return embedBuilder.build();
    }

    /**
     * "Error" embed
     * @return built embed
     */
    public MessageEmbed errorEmbed(){
        return setEmbed("Что-то пошло не так...",
                "Судя по всему произошла непредвиденная ситуация: или у меня нет прав, или выпала внутренняя ошибка.\n" +
                        "Если считаешь что ошибка не с твоей стороны, рекомендую сообщить об этом <@339488218523238410>",
                0xE50278,
                "https://cdn.discordapp.com/emojis/926033819675557949.webp?size=256&quality=lossless");
    }

    /**
     * "No access" embed
     * @return built embed
     */
    public MessageEmbed noAccessEmbed() {
        return setEmbed("Недостаточно прав",
                "Ты забрел на запретную территорию. Беги.",
                0xE50278,
                "https://cdn.discordapp.com/attachments/962656409898602537/1001951656650481854/403.png");
    }

    /**
     * "Done" embed
     * @return built embed
     */
    public MessageEmbed doneEmbed() {
        return setEmbed("Done",
                "Команда выполнена успешно",
                0x00FF00);
    }

    /**
     * "Not implemented" embed
     * @return built embed
     */
    public MessageEmbed notImplementedEmbed() {
        return setEmbed("Not implemented",
                "Этот функционал временно отсутствует",
                0xFF0000,
                "https://cdn.discordapp.com/attachments/962656409898602537/974623596515328021/unknown.png");
    }

}
