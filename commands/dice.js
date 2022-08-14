const { SlashCommandBuilder } = require('discord.js');

module.exports = {
    data: new SlashCommandBuilder()
        .setName('dice')
        .setDescription('Throw a dice'),
    async execute(interaction) {
        const d = Math.floor(Math.random() * 5) + 1;
        await interaction.editReply(`🎲 ${d}`);
    },
};