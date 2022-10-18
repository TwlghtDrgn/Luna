package net.twlghtdrgn.luna.Listeners;

import net.twlghtdrgn.luna.Embeds.Embeds;
import net.twlghtdrgn.luna.Luna;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
    private final Luna luna;

    public MessageListener(Luna luna) {
        this.luna = luna;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String msg = event.getMessage().getContentDisplay().toLowerCase();
        if (!(msg.contains("луна") || msg.contains("лулу"))) return;
        msg = msg.replace("луна","").replace("лулу","").replace(",","").replace("  ",", ");

        String user = event.getAuthor().getAsTag();
        luna.getLogger().info("[" + event.getGuild().getName() + "|" + event.getChannel().getName() + "] " + user + " написал сообщение " + msg);

        event.getMessage().replyEmbeds(new Embeds().notImplementedEmbed()).queue();
    }
}