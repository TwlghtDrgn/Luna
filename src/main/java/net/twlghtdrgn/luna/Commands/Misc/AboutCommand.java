package net.twlghtdrgn.luna.Commands.Misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.twlghtdrgn.luna.Luna;
import net.twlghtdrgn.luna.Utils.ASCIIUtil;

import java.awt.*;

// TODO: rework message output
public class AboutCommand extends ListenerAdapter {
    private final Luna luna;

    public AboutCommand(Luna luna) {
        this.luna = luna;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("about")) return;

        EmbedBuilder embed = new EmbedBuilder();
        ASCIIUtil ascii = new ASCIIUtil();
        String art = ascii.drawString("Luna", "*", new ASCIIUtil.Settings(new Font("SansSerif", Font.PLAIN, 24), 64, 24));

        String text =
                """




                        Я - Луна.
                        Играю радио, двигаю луну, когда в настроении то переписываюсь с людьми, брожу по снам и все такое.
                        Работаю на версии API: %api_ver%
                        Также, я нахожусь в %guilds% гильдиях и знаю около %users% пользователей.""";

        text = text.replace("%api_ver%", JDA.class.getPackage().getImplementationVersion())
                .replace("%guilds%", String.valueOf(luna.getShardManager().getGuilds().size()))
                .replace("%users%", String.valueOf(luna.getShardManager().getUsers().size()));

        embed.setTitle("Обо мне")
                .setDescription("```\n"
                        + art + "\n"
                        + text +
                        "\n```")
//                .setFooter("Test")
                .setColor(Color.BLUE);

        event.replyEmbeds(embed.build()).queue();
    }
}
