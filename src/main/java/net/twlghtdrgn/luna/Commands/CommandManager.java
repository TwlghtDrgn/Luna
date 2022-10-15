package net.twlghtdrgn.luna.Commands;

import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.twlghtdrgn.luna.Commands.Media.RadioCommand;
import net.twlghtdrgn.luna.Embeds.Embeds;
import net.twlghtdrgn.luna.Luna;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {
    private final Luna luna;

    public CommandManager(Luna luna) {
        this.luna = luna;

        luna.getShardManager().addEventListener(new RadioCommand(this.luna));
    }

    @Override
    public void onReady(ReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("radio","Starts a radio in your Staged channel"));
        event.getJDA().updateCommands().addCommands(commandData).queue();
        luna.getLogger().info("Commands updated successfully!");
    }
}
