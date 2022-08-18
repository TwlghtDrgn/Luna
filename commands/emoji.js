const { SlashCommandBuilder } = require('discord.js');

module.exports = {
    data: new SlashCommandBuilder()
        .setName('emoji')
        .setDescription('Send emoji that known to me'),
    async execute(interaction) {
        await interaction.reply('Still not implemented yet <:derpyShrug_1:711863356415541269>');
    },
};