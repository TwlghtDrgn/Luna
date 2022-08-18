const { InteractionType, EmbedBuilder, ActionRowBuilder, ButtonBuilder, ButtonStyle } = require('discord.js');
const bugreport = require('../commands/bugreport');
const playerreport = require('../commands/playerreport');
const { reportID } = require('../config.json');

module.exports = {
    name: 'interactionCreate',
    execute(interaction, logger, client) {
        // Read buttons
        if (interaction.isButton()) {
            logger.info(`> Got button interaction from ${interaction.user.tag}: ${interaction.customId}`);
            if (/reportPlus ?/gui.test(interaction.customId)) {
                const btnID = interaction.customId;
                let points = '';
                if (btnID === 'reportPlus5') points = 5;
                if (btnID === 'reportPlus15') points = 15;
                if (btnID === 'reportPlus25') points = 25;
                interaction.update(`${interaction.message}\n${interaction.user.tag} поставил ${points} очков`);
            }
            if (interaction.customId === 'bugReportBtn') { bugreport.execute(interaction); }
            if (interaction.customId === 'playerReportBtn') { playerreport.execute(interaction); }
        }

        // Read and parse modals
        if (interaction.type !== InteractionType.ModalSubmit) return;
        const description = interaction.fields.getTextInputValue('descriptionInput');

        try {
            if (interaction.customId === 'bugReportModal') {
                const title = interaction.fields.getTextInputValue('titleInput');
                const nickname = interaction.fields.getTextInputValue('nicknameInput');
                const reportEmbed = new EmbedBuilder()
                    .setFooter({ text: `${interaction.user.tag}`, iconURL: `${interaction.user.displayAvatarURL()}` })
                    .setTitle(`Баг-репорт | Сервер: ${title} | Ник на сервере: ${nickname}`)
                    .setDescription(`${description}`)
                    .setTimestamp()
                    .setColor('Blue');

                const points = new ActionRowBuilder()
                    .addComponents(
                        new ButtonBuilder()
                            .setCustomId('reportPlus5')
                            .setLabel('+5')
                            .setStyle(ButtonStyle.Success),
                    )
                    .addComponents(
                        new ButtonBuilder()
                            .setCustomId('reportPlus15')
                            .setLabel('+15')
                            .setStyle(ButtonStyle.Success),
                    )
                    .addComponents(
                        new ButtonBuilder()
                            .setCustomId('reportPlus25')
                            .setLabel('+25')
                            .setStyle(ButtonStyle.Success),
                    );

                client.channels.cache.get(reportID).send({ embeds: [reportEmbed], components: [points] });
                interaction.reply({ content: 'Баг-репорт отправлен', ephemeral: true });
            }
            if (interaction.customId === 'playerReportModal') {
                const nickname = interaction.fields.getTextInputValue('nicknameInput');
                const player = interaction.fields.getTextInputValue('playerInput');
                const reportEmbed = new EmbedBuilder()
                    .setFooter({ text: `${interaction.user.tag}`, iconURL: `${interaction.user.displayAvatarURL()}` })
                    .setTitle(`Репорт на игрока: ${nickname} | Отправил: ${player}`)
                    .setDescription(`${description}`)
                    .setTimestamp()
                    .setColor('Red');

                client.channels.cache.get(reportID).send({ embeds: [reportEmbed] });
                interaction.reply({ content: 'Репорт на игрока отправлен', ephemeral: true });
            }
        } catch (e) {
            logger.error(`!! > Modal errored: ${e}`);
        }
    },
};