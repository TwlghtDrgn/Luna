package net.twlghtdrgn.luna.Listeners;

import net.twlghtdrgn.luna.Luna;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.twlghtdrgn.luna.Messages.BrainShopResponse;

@SuppressWarnings("ConstantConditions")
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
        msg = msg
                .replace("луна","")
                .replace("лулу","")
                .replace(",","")
                .replace("  "," ");

        String user = event.getAuthor().getAsTag();
        luna.getLogger().info("[" + event.getGuild().getName() + "|" + event.getChannel().getName() + "] " + user + " написал сообщение " + msg);

        BrainShopResponse messageResponse = new BrainShopResponse(luna);
        String resp = String.valueOf(messageResponse.lunaAPI(event.getAuthor().getId(),msg));

        try {
            event.getMessage().reply(resp).queue();
        } catch (Exception e) {
            event.getJDA().getUserById(event.getMember().getId())
                    .openPrivateChannel().flatMap(
                            channel -> channel.sendMessage("Ну привет\n" +
                                    "Это сообщение не пришло бы, если бы какой-то гений не запретил мне писать в чате.\n" +
                                    "Так что держи сообщение об ошибке в ЛС:\n"
                                    + resp)
                    ).queue();
        }
    }
}