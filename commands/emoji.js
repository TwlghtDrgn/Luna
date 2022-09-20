const { SlashCommandBuilder } = require('discord.js');

module.exports = {
    data: new SlashCommandBuilder()
        .setName('emoji')
        .setDescription('Send emoji that known to me')
        .addStringOption(option =>
            option.setName('emoji')
                .setDescription('Select emoji to send')
                .setRequired(true)
                .addChoices(
                    { name: 'Shrugging Derpy', value: 'emoji_derp' },
                    { name: 'Boulder', value: 'emoji_buldiga' },
                )),
    async execute(interaction) {
        const name = interaction.options.getString('emoji');
        let resp = 'No emoji';

        if (name === 'emoji_buldiga') {
            resp = '<:pwBuldiga:630872804505681931>';
        } else if (name === 'emoji_derp') {
            resp = '<:derpyShrug_0:643766689376501760>';
        }

        await interaction.reply(resp);
    },
};