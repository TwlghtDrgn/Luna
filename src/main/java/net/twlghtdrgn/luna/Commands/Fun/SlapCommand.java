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
        if (!event.isFromGuild()) return;
        if (!event.getName().equals("slap")) return;
        String pinger = event.getUser().getAsMention();
        String pinged = event.getOption("user").getAsUser().getAsMention();
        Boolean isMegaSlap = false;

        if (event.getOption("ismega") != null) isMegaSlap = event.getOption("ismega").getAsBoolean();

        ArrayList<String> resp;
        if (pinged.equals(event.getJDA().getSelfUser().getAsMention())) {
            resp = easterEggResponses();
        } else resp = normalResponses();

        if (isMegaSlap) {
            event.reply("Executed MegaSlap").setEphemeral(true).queue();
            int i = 0;
            String msg = "";
            while (i <= 5){
                int rand = (int) Math.floor(Math.random() * resp.size());
                msg += resp.get(rand)+"\n";
                i++;
            }
            event.getChannel().asTextChannel().sendMessage(msg.replace("%s", pinger).replace("%r", pinged)).queue();
        } else {
            int rand = (int) Math.floor(Math.random() * resp.size());
            event.reply(resp.get(rand).replace("%s", pinger).replace("%r", pinged)).queue();
        }
    }

    public ArrayList<String> normalResponses() {
        ArrayList<String> resp = new ArrayList<>(Arrays.asList(
                "%s заорал вслед %r 'КУПИ СЛОНА!!!111'",
                "%s взял кирпич и швырнул его в %r",
                "%s устал ждать прибытия %r и пришел к нему во двор",
                "%s старается поймать %r за хвост",
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

        return resp;
    }

    public ArrayList<String> easterEggResponses() {
        ArrayList<String> resp = new ArrayList<>(Arrays.asList(
                "%r теперь гонится за %s, так как этот гений подкрался к ней сзади и напугал её",
                "Знаете, я испытываю ну оочень сильное желание сослать %s на Солнце. Просто по приколу =)",
                "%r продала компьютер %s за его назойливость",
                "%r подозревает %s в том, что он использует какие-то вещи ну оооочень не по назначению",
                "\\**звуки начала боя покемонов*\\*\n%r - 99999hp \t%s - 852hp"
        ));

        return resp;
    }
}
