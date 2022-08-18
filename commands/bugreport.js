const { SlashCommandBuilder, ModalBuilder, TextInputBuilder, TextInputStyle, ActionRowBuilder } = require('discord.js');

module.exports = {
    data: new SlashCommandBuilder()
        .setName('bugreport')
        .setDescription('Use this to report bugs'),
    async execute(interaction) {
        const modal = new ModalBuilder()
            .setCustomId('bugReportModal')
            .setTitle('Отправка баг-репорта');

        // Components
        const titleInput = new TextInputBuilder()
            .setCustomId('titleInput')
            .setLabel('Сервер, на котором был обнаружен баг')
            .setStyle(TextInputStyle.Short)
            .setPlaceholder('Креатив, выживание, бедварс, лобби...');

        const nicknameInput = new TextInputBuilder()
            .setCustomId('nicknameInput')
            .setLabel('Введи свой ник')
            .setStyle(TextInputStyle.Short)
            .setPlaceholder('Ник в игре, если что');

        const descriptionInput = new TextInputBuilder()
            .setCustomId('descriptionInput')
            .setLabel('Описание бага')
            .setPlaceholder('Опиши найденый баг как можно подробнее. Можно приложить видео (YouTube) или скриншот в виде ссылки')
            .setStyle(TextInputStyle.Paragraph);

        const firstActionRow = new ActionRowBuilder().addComponents(titleInput);
        const secondActionRow = new ActionRowBuilder().addComponents(nicknameInput);
        const thirdActionRow = new ActionRowBuilder().addComponents(descriptionInput);

        modal.addComponents(firstActionRow, secondActionRow, thirdActionRow);

        await interaction.showModal(modal);
    },
};