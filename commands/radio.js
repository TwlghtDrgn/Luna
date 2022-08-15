const { SlashCommandBuilder, EmbedBuilder } = require('discord.js');
const { AudioPlayerStatus, joinVoiceChannel, createAudioPlayer, createAudioResource } = require('@discordjs/voice');
const { stageID, guildID } = require('../config.json');
const fetch = require('node-fetch');
const wait = require('node:timers/promises').setTimeout;

module.exports = {
    data: new SlashCommandBuilder()
        .setName('radio')
        .setDescription('It says everything in its name'),
    async execute(interaction, client, logger) {
        const doneEmbed = new EmbedBuilder()
            .setColor('Green')
            .setTitle('Done')
            .setDescription('Команда выполнена успешно')
            .setThumbnail('https://derpicdn.net/img/view/2014/11/23/770308.png')
            .setTimestamp();

        const errorEmbed = new EmbedBuilder()
            .setColor('DarkVividPink')
            .setTitle('Что то пошло не так...')
            .setDescription('Судя по всему произошла ошибка: или у меня нет прав, или попалась ошибка в коде. Извини, и по возможности сообщи об этом <@339488218523238410>')
            .setTimestamp()
            .setThumbnail('https://cdn.discordapp.com/emojis/926033819675557949.webp?size=256&quality=lossless');

        const noAccessEmbed = new EmbedBuilder()
            .setColor('DarkVividPink')
            .setTitle('Недостаточно прав')
            .setDescription('Ты забрел на запретную территорию. Беги.')
            .setTimestamp()
            .setThumbnail('https://cdn.discordapp.com/attachments/962656409898602537/1001951656650481854/403.png');

        if (interaction.user.id != ('339488218523238410' || '251375740673720321')) {
            await wait(1000);
            await interaction.editReply({ embeds: [noAccessEmbed] });
            return;
        }

        const url = 'https://radio.twilightdev.ru/api/nowplaying/1';
        const settings = { method: 'Get' };

        // get the voice channel ids
        const voiceChannelId = stageID;
        const voiceChannel = client.channels.cache.get(voiceChannelId);
        const guildId = guildID;

        // create audio player
        const player = createAudioPlayer();

        player.on(AudioPlayerStatus.Playing, () => {
            logger.info('[Radio] > The audio player has started playing!');
        });

        player.on('error', error => {
            logger.error(`!! [Radio] > Error: ${error.message} with resource`);
        });

        // create audio source
        const resource = createAudioResource('http://192.168.0.5:8000/streamhq.mp3', {
            inlineVolume: true,
        });

        // create the connection to the voice channel
        const connection = joinVoiceChannel({
            channelId: voiceChannelId,
            guildId: guildId,
            adapterCreator: voiceChannel.guild.voiceAdapterCreator,
        });

        // Subscribe the connection to the audio player (will play audio on the voice connection)
        const subscription = connection.subscribe(player);

        await wait(2000);

        let repeater = true;

        try {
            await voiceChannel.guild.stageInstances.fetch(stageID, { cache: true });
            await wait(2000);
            logger.info('[Radio] > Stage found, using old one...');
        } catch (e) {
            if ((e.toString()).indexOf('Unknown Stage Instance') > 0) {
                voiceChannel.createStageInstance({ topic: 'Radio', privacyLevel: 2 });
            } else { logger.error(e); }
        }

        await wait(1000);

        try {
            // play audio
            player.play(resource);

            await interaction.editReply({ embeds: [doneEmbed] });

            // set full access to Staged channel
            await voiceChannel.guild.members.me.voice.setSuppressed(false);

            await wait(2000);

            while (repeater) {
                // Sanity fixer
                if (voiceChannel.guild.members.me.voice.suppress) {
                    logger.info('[Radio] > Someone moved me to listeners. Fixing it...');
                    voiceChannel.guild.members.me.voice.setSuppressed(false);
                    await wait(2000);
                } else {
                    // Wait 20 seconds and check again + retrieve JSON
                    await fetch(url, settings)
                        .then(res => res.json())
                        .then((json) => {
                            if (json.live.is_live) {
                                voiceChannel.guild.stageInstances.edit(stageID, { topic: `[LIVE] ${json.live.streamer_name}: ${json.now_playing.song.text}` });
                            } else {
                                voiceChannel.guild.stageInstances.edit(stageID, { topic: `${json.now_playing.song.text}` });
                            }
                        });
                    await wait(20000);
                }
            }
        } catch (e) {
            await interaction.editReply({ embeds: [errorEmbed] });
            logger.error(`!! [Radio] > ${e}`);
            await wait(1000);
            repeater = false;
            if (subscription) {
                // Unsubscribe after 5 seconds (stop playing audio on the voice connection)
                setTimeout(() => subscription.unsubscribe(), 5_000);
            }
            // voiceChannel.guild.stageInstance.delete(stageID);
            player.stop();
            connection.destroy();
        }
    },
};