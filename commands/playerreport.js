const { SlashCommandBuilder, ModalBuilder, TextInputBuilder, TextInputStyle, ActionRowBuilder } = require('discord.js');

module.exports = {
    data: new SlashCommandBuilder()
        .setName('playerreport')
        .setDescription('Use this to report players'),
    async execute(interaction) {
        const modal = new ModalBuilder()
            .setCustomId('playerReportModal')
            .setTitle('Отправка репорта на игрока');

        // Components
        const nicknameInput = new TextInputBuilder()
            .setCustomId('nicknameInput')
            .setLabel('Кого обвиняем?')
            .setStyle(TextInputStyle.Short)
            .setPlaceholder('Один или несколько ников через запятую');

        const playerInput = new TextInputBuilder()
            .setCustomId('playerInput')
            .setLabel('Введи свой ник')
            .setStyle(TextInputStyle.Short)
            .setPlaceholder('Ник в игре, если что');

        const descriptionInput = new TextInputBuilder()
            .setCustomId('descriptionInput')
            .setLabel('В чем обвиняем?')
            .setPlaceholder('Опиши суть проблемы, что произошло. Рекомендуем приложить видео (YouTube) или скриншот в виде ссылки')
            .setStyle(TextInputStyle.Paragraph);

        const firstActionRow = new ActionRowBuilder().addComponents(playerInput);
        const secondActionRow = new ActionRowBuilder().addComponents(nicknameInput);
        const thirdActionRow = new ActionRowBuilder().addComponents(descriptionInput);

        modal.addComponents(firstActionRow, secondActionRow, thirdActionRow);

        await interaction.showModal(modal);
    },
};