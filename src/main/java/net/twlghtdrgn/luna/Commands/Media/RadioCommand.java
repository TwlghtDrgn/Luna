package net.twlghtdrgn.luna.Commands.Media;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.StageInstance;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.StageChannel;
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
        if (!event.isFromGuild()) return;
        if (!event.getName().equals("radio")) return;
        Guild guild = event.getGuild();
        Member member = event.getMember();

        if (!event.getOption("enabled").getAsBoolean()) {
            if (guild.getAudioManager().getConnectedChannel() != null) {
                StageChannel stage = guild.getAudioManager().getConnectedChannel().asStageChannel();
                stage.getStageInstance().delete().queue();
                guild.getAudioManager().closeAudioConnection();
                event.replyEmbeds(embeds.setEmbed("Радио","Вещание успешно приостановлено!",0x00FF00)).setEphemeral(true).queue();
            } else event.replyEmbeds(radioEmbed("Невозможно приостановить вещание - оно не запущено")).setEphemeral(true).queue();
            return;
        }

        if (!member.getVoiceState().inAudioChannel()) {
            event.replyEmbeds(radioEmbed("Укажите Staged-канал для подключения (подключитесь к нему)")).setEphemeral(true).queue();
            return;
        }

        if (!member.getVoiceState().getChannel().asStageChannel().getType().equals(ChannelType.STAGE)) {
            event.replyEmbeds(radioEmbed("Данный функционал доступен только в Staged-канале")).setEphemeral(true).queue();
            return;
        }

        if (event.getGuild().getAudioManager().isConnected()) {
            event.getGuild().getAudioManager().closeAudioConnection();
        }

        event.deferReply().setEphemeral(true).queue();

        StageChannel stage = member.getVoiceState().getChannel().asStageChannel();
        AudioManager manager = guild.getAudioManager();
        StageInstance stageInstance = stage.getStageInstance();

        if (stage.getStageInstance() == null) {
            stage.createStageInstance("Radio")
                    .setPrivacyLevel(StageInstance.PrivacyLevel.GUILD_ONLY).queue();
        }

        event.getHook().editOriginalEmbeds(embeds.notImplementedEmbed()).queue();

//        try {
////            manager.setSendingHandler();
//            manager.openAudioConnection(stage);
//            event.getHook().editOriginalEmbeds(embeds.doneEmbed()).queue();
//        } catch (Exception e) {
//            luna.getLogger().error(e.toString());
//            if (event.getHook().isExpired()) {
//                luna.getLogger().warn("Something happened with Radio module, but the hook is expired");
//                event.getJDA().getUserById(member.getId()).openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(embeds.errorEmbed()));
//            } else event.getHook().editOriginalEmbeds(embeds.errorEmbed()).queue();
//        }


    }

}
