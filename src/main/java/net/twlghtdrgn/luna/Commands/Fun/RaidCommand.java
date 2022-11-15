package net.twlghtdrgn.luna.Commands.Fun;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.twlghtdrgn.luna.Luna;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RaidCommand extends ListenerAdapter {
    private final Luna luna;
    private final BlockingQueue<Member> queue = new LinkedBlockingQueue<>();
    private final ArrayList<Member> joined = new ArrayList<>();
    int max_users = 1;
    public RaidCommand(Luna luna) {
        this.luna = luna;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.isFromGuild()) return;
        if (event.getName().equals("raid")) return;
        if (!raidQueue.isAlive()) raidQueue.start();
        Member member = event.getMember();

        queue.add(member);

        String message = "<%s> присоединился к рейду. %state!";
        String state = "Осталось %l участников до начала рейда";
        if (joined.size() == max_users - 1) state = "Начинаем рейд";
        event.reply(message.replace("%s", member.getId()).replace("%state",state).replace("%l",String.valueOf(max_users - joined.size())));
    }

    Thread raidQueue = new Thread(() -> {
        while (true) {
            try {
                 joined.add(queue.take());

                 if (joined.size() == max_users - 1) {
                     for (Member m : joined) {
//                         m.getGuild().getGuildChannelById();
                     }
                 }
            } catch (InterruptedException e) {

            }
        }
    });
}
