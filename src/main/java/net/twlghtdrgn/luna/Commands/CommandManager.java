package net.twlghtdrgn.luna.Commands;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
//import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.twlghtdrgn.luna.Commands.Fun.DiceCommand;
//import net.twlghtdrgn.luna.Commands.Fun.EmojiCommand;
import net.twlghtdrgn.luna.Commands.Fun.SlapCommand;
import net.twlghtdrgn.luna.Commands.Media.RadioCommand;
import net.twlghtdrgn.luna.Luna;
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
        luna.getShardManager().addEventListener(new DiceCommand(this.luna));
        luna.getShardManager().addEventListener(new SlapCommand(this.luna));
//        luna.getShardManager().addEventListener(new EmojiCommand(this.luna));
    }

    @Override
    public void onReady(ReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("radio","Starts a radio in your Staged channel. Currently broken."));
        commandData.add(Commands.slash("dice","Draw a number from 1 to 6"));
        commandData.add(Commands.slash("slap","Slap someone (inspired by Cadyr, https://udj.at/cadyr)")
                .addOption(OptionType.USER, "user","Person who you want to slap", true));
// TODO: fix emoji command
//        commandData.add(Commands.slash("emoji","Send emoji known to me")
//                .addOptions(
//                    new OptionData(OptionType.STRING,"twlghtdrgn","TwlghtDrgn's emojis",false)
//                            .addChoice("Shrugging Derpy","emoji_derp")
//                            .addChoice("Shrugging Luna","emoji_lulu")
//                            .addChoice("Repel","emoji_repel")
//                            .addChoice("Thinking Lyra","emoji_lyra")
//                            .addChoice("SUS","emoji_sus")
//                            .addChoice("Facehoof","emoji_facehoof")
//                            .addChoice("Uno Reverse Card","emoji_uno")
//                            .addChoice("Dad that watching your browser history","emoji_fbi")
//                            .addChoice("FullHD 1080P","emoji_fhd")
//                            .addChoice("UltraHD 4K","emoji_uhd")
//                            .addChoice("Dead Chat","emoji_deadchat"),
//                    new OptionData(OptionType.STRING,"puffers","PuffersWorld emojis",false)
//                            .addChoice("Boulder", "boulder")
//                )
//        );
        event.getJDA().updateCommands().addCommands(commandData).queue();
        luna.getLogger().info("Commands updated successfully!");
    }
}
