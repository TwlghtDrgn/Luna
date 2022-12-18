package net.twlghtdrgn.luna.Listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.twlghtdrgn.luna.Luna;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class BirthdayListener extends ListenerAdapter {
    private final Luna luna;

    public BirthdayListener(Luna luna) {
        this.luna = luna;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;
        if (!event.getAuthor().getId().equals("339488218523238410")) return;
        if (!event.getMessage().getContentRaw().toLowerCase().startsWith("с др")) return;
        TextChannel channel = event.getChannel().asTextChannel();

        Thread bDayDelay = new Thread(() -> {
            try {
                TimeUnit.MINUTES.sleep((long) Math.floor(Math.random() * 4) + 1);
                channel.sendMessageEmbeds(bdayEmbed()).queue();
            } catch (InterruptedException e) {
                throw new RuntimeException("Uncaught exception: ", e);
            }
        });

        bDayDelay.setPriority(6);
        bDayDelay.start();
    }

    private MessageEmbed bdayEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder
                .setImage("https://derpicdn.net/img/download/2021/10/3/2715435.png")
                .setColor(Color.BLUE);

        return embedBuilder.build();
    }
}
