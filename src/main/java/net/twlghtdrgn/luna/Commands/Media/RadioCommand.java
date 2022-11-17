package net.twlghtdrgn.luna.Commands.Media;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.StageInstance;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.StageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.managers.StageInstanceManager;
import net.twlghtdrgn.luna.Audio.AudioPlayerImpl;
import net.twlghtdrgn.luna.Audio.AudioPlayerSendHandler;
import net.twlghtdrgn.luna.Embeds.Embeds;
import net.twlghtdrgn.luna.Luna;
import net.twlghtdrgn.luna.Utils.JSONParser;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ConstantConditions")
public class RadioCommand extends ListenerAdapter {
    private final Luna luna;
    Embeds embeds = new Embeds();
    AudioPlayerImpl player = new AudioPlayerImpl();

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
                destroyConnection(guild);
                event.replyEmbeds(embeds.setEmbed("Радио","Вещание успешно приостановлено!",0x00FF00)).setEphemeral(true).queue();
            } else event.replyEmbeds(radioEmbed("Невозможно приостановить вещание - оно не запущено")).setEphemeral(true).queue();
            return;
        }

        if (!member.getVoiceState().inAudioChannel()) {
            event.replyEmbeds(radioEmbed("Укажите Staged-канал для подключения (подключитесь к нему)")).setEphemeral(true).queue();
            return;
        }

        if (!member.getVoiceState().getChannel().getType().equals(ChannelType.STAGE)) {
            event.replyEmbeds(radioEmbed("Данный функционал доступен только в Staged-канале")).setEphemeral(true).queue();
            return;
        }

        event.deferReply().setEphemeral(true).queue();
        AudioManager manager = guild.getAudioManager();

        if (manager.isConnected()) {
            manager.closeAudioConnection();
        }

        StageChannel stage = member.getVoiceState().getChannel().asStageChannel();

        if (stage.getStageInstance() == null) {
            stage.createStageInstance("Radio")
                    .setPrivacyLevel(StageInstance.PrivacyLevel.GUILD_ONLY).queue();
        }

        try {
            manager.setSendingHandler(new AudioPlayerSendHandler(player.createAudioPlayer()));
            manager.openAudioConnection(stage);
            event.getHook().editOriginalEmbeds(embeds.doneEmbed()).queue();

            Thread updateData = new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    stage.requestToSpeak().complete();
                    StageInstance stageInstance = stage.getStageInstance();
                    while (manager.isConnected()) {
                        if (stage.getStageInstance() != null) {
                            updateTitle(stageInstance);
                        }
                        TimeUnit.MILLISECONDS.sleep(20000);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException("Uncaught exception:", e);
                }
            });

            updateData.setPriority(6);
            updateData.start();
        } catch (Exception e) {
            luna.getLogger().error(e.toString());
            destroyConnection(guild);
            if (event.getHook().isExpired()) {
                luna.getLogger().warn("Something happened with Radio module, but the hook is expired");
                event.getJDA().getUserById(member.getId()).openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(embeds.errorEmbed())).queue();
            } else event.getHook().editOriginalEmbeds(embeds.errorEmbed()).queue();
        }
    }

    private void destroyConnection(Guild guild) {
        try {
            StageChannel stage = guild.getAudioManager().getConnectedChannel().asStageChannel();
            if (stage.getStageInstance() != null) {
                stage.getStageInstance().delete().queue();
            }
            guild.getAudioManager().closeAudioConnection();
        } catch (Exception e) {
            luna.getLogger().error(e.toString());
        }
    }

    private void updateTitle(StageInstance stage) {
        StageInstanceManager manager = stage.getManager();
        ArrayList<String> data = radioData();
        try {
            String title;
            if (data.get(0).length() > 2) title = data.get(0) + " - " + data.get(1);
                else title = data.get(1);

            if (!(stage.getTopic().length() > 1) | !stage.getGuild().getAudioManager().isConnected()) return;

            if (data.get(2).equals("true")) manager.setTopic("\uD83D\uDD34 " + data.get(3) + ": " + title).queue();
                else manager.setTopic(title).queue();
        } catch (Exception e) {
            manager.setTopic("Data is unavailable").queue();
            luna.getLogger().error(e.getMessage());
        }
    }

    private ArrayList<String> radioData() {
        try {
            ArrayList<String> info = new ArrayList<>();
            URL url = new URL("https://radio.twilightdev.ru/api/nowplaying/1");
            String jsResp = new JSONParser(luna).JSON(url);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode response = mapper.readTree(jsResp);

            info.add(response.findPath("now_playing").findValuesAsText("artist").get(0));
            info.add(response.findPath("now_playing").findValuesAsText("title").get(0));
            info.add(response.findPath("live").findValuesAsText("is_live").get(0));
            info.add(response.findPath("live").findValuesAsText("streamer_name").get(0));

            return info;
        } catch (Exception e) {
            luna.getLogger().error(e.toString());
            return null;
        }
    }
}
