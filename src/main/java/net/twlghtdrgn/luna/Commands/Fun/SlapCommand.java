package net.twlghtdrgn.luna.Commands.Fun;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.twlghtdrgn.luna.Luna;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("ConstantConditions")
public class SlapCommand extends ListenerAdapter {
    private final Luna luna;

    public SlapCommand(Luna luna) {
        this.luna = luna;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.isFromGuild()) return;
        if (!event.getName().equals("slap")) return;
        String pinger = event.getUser().getAsMention();
        String pinged = event.getOption("user").getAsUser().getAsMention();
        boolean isMegaSlap = false;

        if (event.getOption("ismega") != null) isMegaSlap = event.getOption("ismega").getAsBoolean();

        ArrayList<String> resp;
        if (pinged.equals(event.getJDA().getSelfUser().getAsMention())) {
            resp = easterEggResponses();
        } else resp = normalResponses();

        if (isMegaSlap) {
            event.reply("Executed MegaSlap").setEphemeral(true).queue();
            int i = 0;
            while (i <= 3){
                int rand = (int) Math.floor(Math.random() * resp.size());
                event.getChannel().asTextChannel().sendMessage(resp.get(rand).replace("%s", pinger).replace("%r", pinged)).queue();
                i++;
            }
        } else {
            int rand = (int) Math.floor(Math.random() * resp.size());
            event.reply(resp.get(rand).replace("%s", pinger).replace("%r", pinged)).queue();
        }
    }

    public ArrayList<String> normalResponses() {
        return new ArrayList<>(Arrays.asList(
                "%s напомнил %r про давнее обещание",
                "%s устал ждать %r и пришел к нему во двор",
                "%s пошел искать %r",
                "%s не дождался %r и пошел к нему домой",
                "%s стучится в дверь квартиры %r, ожидая ответа",
                "%s швырнул тапком в %r",
                "%s пришел в комнату к %r, тихонько включил компьютер и начал играть в доту.",
                "%s подкрался сзади и резко крикнул %r на ухо какую-то неразборчивую хрень",
                "%s кинул вонючие носки в сумку к %r и убежал, ехидно хихикая",
                "%s пытается разбудить %r в три часа ночи",
                "Злой и сонный %r угрожая тапком несется за %s по улице, ибо нефиг будить посреди ночи",
                "%r пытается убежать от %s",
                "%s старается поймать %r за хвост",
                "%s взял кирпич и швырнул его в %r",
                "%s крикнул вслед %r 'КУПИ СЛОНА!!!111'"
        ));
    }

    public ArrayList<String> easterEggResponses() {
        return new ArrayList<>(Arrays.asList(
                "%r теперь гонится за %s, так как этот гений подкрался к ней сзади и напугал её",
                "Знаете, я испытываю ну оочень сильное желание сослать %s на Солнце. Просто по приколу :smile:",
                "%r продала компьютер %s за его назойливость",
                "%r подозревает %s в том, что он использует какие-то вещи ну оооочень не по назначению",
                "\\**звуки начала боя покемонов*\\*\n%r - 99999hp \t%s - 852hp"
        ));
    }
}
