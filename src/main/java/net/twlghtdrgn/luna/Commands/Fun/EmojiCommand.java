package net.twlghtdrgn.luna.Commands.Fun;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.twlghtdrgn.luna.Luna;

public class EmojiCommand extends ListenerAdapter {
    private final Luna luna;

    public EmojiCommand(Luna luna) {
        this.luna = luna;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("emoji")) return;
        // TODO: Find a way to use options without getting an error
        Object twlght = event.getOption("twlghtdrgn");
        Object puff = event.getOption("puffers");

        if (twlght != null) {
            String val = twlght.toString();
            switch (val) {
                case ("emoji_derp") -> {
                    event.reply("<:derpyShrug_0:643766689376501760>").queue();
                }
            }

            return;
        }
        if (puff != null) {
            String val = puff.toString();
            switch (val) {
                case ("emoji_buldiga") -> {
                    event.reply("<:pwBuldiga:630872804505681931>").queue();
                }
            }
            return;
        }
        event.reply("Emoji is not selected or does not exist. Use one of the categories to select emoji")
                .setEphemeral(true).queue();
    }
}
