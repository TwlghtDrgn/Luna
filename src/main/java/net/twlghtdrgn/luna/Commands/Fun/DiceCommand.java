package net.twlghtdrgn.luna.Commands.Fun;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.twlghtdrgn.luna.Luna;

public class DiceCommand extends ListenerAdapter {
    private final Luna luna;

    public DiceCommand(Luna luna) {
        this.luna = luna;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("dice")) return;

        int draw = (int) Math.floor(Math.random() * 6) + 1;

        event.reply("\uD83C\uDFB2 " + draw).queue();
    }
}
