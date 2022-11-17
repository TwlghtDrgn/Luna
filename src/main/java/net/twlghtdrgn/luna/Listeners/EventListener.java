package net.twlghtdrgn.luna.Listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.twlghtdrgn.luna.Luna;

@SuppressWarnings("ConstantConditions")
public class EventListener extends ListenerAdapter {
    private final Luna luna;

    public EventListener(Luna luna) {
        this.luna = luna;
    }

    @Override
    public void onReady(ReadyEvent event) {
        luna.getLogger().info("Using " + event.getJDA().getShardInfo().getShardTotal()  + " shard(s), found " + event.getJDA().getGuilds().size() + " guild(s)");
        luna.getLogger().info("Initialization complete! Logged in as " + event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getInteraction().isFromGuild()) return;
        luna.getLogger().info("[" + event.getGuild().getName() + "|" + event.getChannel().getName() + "] " + event.getUser().getAsTag() + " выполнил команду " + event.getName());
    }
}
