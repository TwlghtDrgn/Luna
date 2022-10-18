package net.twlghtdrgn.luna.Commands.Fun;

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
        String pinger = event.getUser().getName();
        String pinged = event.getOption("user").getAsUser().getAsMention();

        ArrayList<String> resp = new ArrayList<>(Arrays.asList(
                "%s взял кирпич и швырнул его в %r",
                "%s устал ждать прибытия %r и пришел к нему во двор",
                "%s пытается свой шанс поймать %r за хвост",
                "%s потерял брелок, который ему подарил %r",
                "%r пытается убежать от %s",
                "%s пошел искать %r",
                "%s выкрикнул имя %r",
                "%s не дождался %r и пошел к нему домой",
                "%s стучится в дверь квартиры %r, ожидая ответа",
                "%s пришел в комнату к %r, тихонько включил компьютер и начал играть в Minecraft с читами. Стоп, что?!",
                "%s напомнил %r про давнее обещание",
                "%s швырнул тапком в %r",
                "%s кинул вонючие носки в сумку к %r и убежал, ехидно хихикая",
                "%s пытается разбудить %r в три часа ночи",
                "Злой и сонный %r угрожая тапком несется за %s по улице, ибо нефиг будить посреди ночи",
                "%s подкрался сзади и резко крикнул %r на ухо какую-то неразборчивую хрень"
        ));

        int rand = (int) Math.floor(Math.random() * resp.size());

        event.reply(resp.get(rand).replace("%s",pinger).replace("%r",pinged)).queue();
    }
}
