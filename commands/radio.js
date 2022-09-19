const { SlashCommandBuilder } = require('discord.js');
const { AudioPlayerStatus, joinVoiceChannel, createAudioPlayer, createAudioResource } = require('@discordjs/voice');
const fetch = require('node-fetch');
const wait = require('node:timers/promises').setTimeout;

module.exports = {
    data: new SlashCommandBuilder()
        .setName('radio')
        .setDescription('It says everything in its name'),
    async execute(interaction, client, logger, embed) {
        if (!interaction.guild.roles.cache.some(role => role.name === 'Staff Team' || 'Admins' || 'Admin')) {
            await interaction.reply({ embeds: [embed.noAccessEmbed] });
            return;
        }

        await interaction.deferReply();
        await wait(1000);

        const url = 'https://radio.twilightdev.ru/api/nowplaying/1';
        const settings = { method: 'Get' };

        // get the voice channel ids
        const voiceChannelId = interaction.member.voice.channelId;
        const voiceChannel = client.channels.cache.get(voiceChannelId);
        const guildId = interaction.guild.id;

        if (voiceChannelId === null) {
            await interaction.editReply({ embeds: [embed.templateEmbed.setTitle('Ошибка').setDescription('Укажите Staged-канал для подключения (подключитесь к нему)').setColor('DarkVividPink')] });
            return;
        }

        if (interaction.guild.channels.cache.get(voiceChannelId).type != 13) {
            await interaction.editReply({ embeds: [embed.templateEmbed.setTitle('Ошибка').setDescription('Данный функционал доступен только в Staged-канале').setColor('DarkVividPink')] });
            return;
        }

        // create audio player
        const player = createAudioPlayer();

        player.on(AudioPlayerStatus.Playing, () => {
            logger.info(`[${interaction.guild.name} | Radio] > The audio player has started playing!`);
        });

        player.on('error', error => {
            logger.error(`!! [${interaction.guild.name} | Radio] > Error: ${error.message} with resource`);
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
            await voiceChannel.guild.stageInstances.fetch(voiceChannelId, { cache: true });
            await wait(2000);
            logger.info(`[${interaction.guild.name} | Radio] > Stage found, using old one...`);
        } catch (e) {
            if ((e.toString()).indexOf('Unknown Stage Instance') > 0) {
                voiceChannel.createStageInstance({ topic: 'Radio', privacyLevel: 2 });
            } else { logger.error(e); }
        }

        await wait(1000);

        try {
            // play audio
            player.play(resource);

            await interaction.editReply({ embeds: [embed.doneEmbed] });

            await wait(1000);

            while (repeater) {
                // Sanity fixer
                if (voiceChannel.guild.members.me.voice.suppress) {
                    logger.info(`[${interaction.guild.name} | Radio] > Becoming a speaker`);
                    voiceChannel.guild.members.me.voice.setSuppressed(false);
                    await wait(2000);
                } else {
                    // Wait 20 seconds and check again + retrieve JSON
                    await fetch(url, settings)
                        .then(res => res.json())
                        .then((json) => {
                            if (json.now_playing.song.text === '') {
                                voiceChannel.guild.stageInstances.edit(voiceChannelId, { topic: 'No data' });
                            } else if (json.live.is_live) {
                                voiceChannel.guild.stageInstances.edit(voiceChannelId, { topic: `[LIVE] ${json.live.streamer_name}: ${json.now_playing.song.text}` });
                            } else {
                                voiceChannel.guild.stageInstances.edit(voiceChannelId, { topic: `${json.now_playing.song.text}` });
                            }
                        });
                    if (!player.checkPlayable()) { throw new Error('Player stopped'); }
                    await wait(20000);
                }
            }
        } catch (e) {
            logger.error(`!! [${interaction.guild.name} | Radio] > ${e}`);
            await wait(1000);
            repeater = false;
            if (subscription) {
                // Unsubscribe after 5 seconds (stop playing audio on the voice connection)
                setTimeout(() => subscription.unsubscribe(), 5_000);
            }
            player.stop();
            connection.destroy();
            // Message about an error
            try {
                await interaction.editReply({ embeds: [embed.errorEmbed] });
            } catch (error) {
                await client.users.fetch(interaction.user.id, false).then((user) => {
                    user.send({ content: 'Вы получили это сообщение, так как команда `/radio` была выполнена Вами более 15 минут назад.', embeds: [embed.errorEmbed] });
                });
            }
        }
    },
};