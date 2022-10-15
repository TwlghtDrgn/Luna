package net.twlghtdrgn.luna.Commands.Media;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.twlghtdrgn.luna.Embeds.Embeds;
import net.twlghtdrgn.luna.Luna;

public class RadioCommand extends ListenerAdapter {
    private final Luna luna;
    Embeds embeds = new Embeds();
    public RadioCommand(Luna luna) {
        this.luna = luna;
    }

    private MessageEmbed radioEmbed(String desc) {
        return embeds.setEmbed("Ошибка",
                desc,
                0xE50278);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("radio")) return;
        Guild guild = event.getGuild();
        Member member = event.getMember();

//        if (event.getGuild().getAudioManager().isConnected()) {
//            event.getGuild().getAudioManager().closeAudioConnection();
//        }
        if (!member.getVoiceState().inAudioChannel()) {
            event.replyEmbeds(radioEmbed("Укажите Staged-канал для подключения (подключитесь к нему)")).setEphemeral(true).queue();
            return;
        }

        if (!member.getVoiceState().getChannel().asStageChannel().getType().equals(ChannelType.STAGE)) {
            event.replyEmbeds(radioEmbed("Данный функционал доступен только в Staged-канале")).setEphemeral(true).queue();
            return;
        }

        event.deferReply().setEphemeral(true).queue();
//        AudioManager manager = guild.getAudioManager();
//        VoiceChannel stage = member.getVoiceState().getChannel().asVoiceChannel();
//
//        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
//        AudioSourceManagers.registerRemoteSources(playerManager);
//
//        AudioPlayer player = playerManager.createPlayer();

        event.getHook().editOriginalEmbeds(embeds.notImplementedEmbed()).queue();


        if (event.getHook().isExpired()) {
            luna.getLogger().warn("Something happened with Radio module, but the hook is expired");
            event.getJDA().getUserById(member.getId()).openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(embeds.errorEmbed()));
        }
    }

}
