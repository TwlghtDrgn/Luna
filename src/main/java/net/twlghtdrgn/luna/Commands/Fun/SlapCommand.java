package net.twlghtdrgn.luna.Commands.Fun;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.twlghtdrgn.luna.Luna;

import java.util.ArrayList;
import java.util.Arrays;

public class SlapCommand extends ListenerAdapter {
    private final Luna luna;

    public SlapCommand(Luna luna) {
        this.luna = luna;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("slap")) return;
        String pinger = event.getUser().getAsMention();
        String pinged = event.getOption("user").getAsUser().getAsMention();

        ArrayList<String> resp = new ArrayList<>(Arrays.asList(
                " взял кирпич и швырнул его в ",
                " устал ждать прихода ",
                " упустил свой шанс поймать ",
                " потерял брелок, подаренный ему от ",
                " пытается убежать от ",
                " пошел искать ",
                " выкрикнул имя "
        ));

        int rand = (int) Math.floor(Math.random() * resp.size());

        event.reply(pinger + resp.get(rand) + pinged).queue();
    }
}
