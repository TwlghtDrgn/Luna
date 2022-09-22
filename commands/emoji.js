const { SlashCommandBuilder } = require('discord.js');

module.exports = {
    data: new SlashCommandBuilder()
        .setName('emoji')
        .setDescription('Send emoji that known to me')
        .addStringOption(option =>
            option.setName('twlghtdrgn')
                .setDescription('TwlghtDrgn\'s emojis')
                .setRequired(false)
                .addChoices(
                    { name: 'Shrugging Derpy', value: 'emoji_derp' },
                    { name: 'Shrugging Luna', value: 'emoji_lulu' },
                    { name: 'Shrugging Celestia', value: 'emoji_tia' },
                    { name: 'Repel', value: 'emoji_repel' },
                    { name: 'Thinking Lyra', value: 'emoji_lyra' },
                    { name: 'Sus', value: 'emoji_sus' },
                    { name: 'Facehoof', value: 'emoji_facehoof' },
                    { name: 'Uno Reverse card', value: 'emoji_uno' },
                    { name: 'Dad watching your browser history', value: 'emoji_fbi' },
                    { name: 'FullHD 1080P', value: 'emoji_fhd' },
                    { name: 'UltraHD 4K', value: 'emoji_uhd' },
                    { name: 'Dead Chat', value: 'emoji_deadchat' },
                ))
        .addStringOption(option =>
            option.setName('puffers')
                .setDescription('PuffersWorld emojis')
                .setRequired(false)
                .addChoices(
                    { name: 'Boulder', value: 'emoji_buldiga' },
                )),
    async execute(interaction) {
        const twlght = interaction.options.getString('twlghtdrgn');
        const puff = interaction.options.getString('puffers');
        let resp;

        switch (twlght) {
        // TwlghtDrgn's emotes
        case 'emoji_derp':
            resp = '<:derpyShrug_0:643766689376501760>';
            await interaction.reply(resp);
            return;
        case 'emoji_lulu':
            resp = '<:luluShrug:711862558826692660>';
            await interaction.reply(resp);
            return;
        case 'emoji_tia':
            resp = '<:tiaShrug:643786229548056576>';
            await interaction.reply(resp);
            return;
        case 'emoji_repel':
            resp = '<:repel:911605102383595540>';
            await interaction.reply(resp);
            return;
        case 'emoji_lyra':
            resp = '<:lyraThinking:860056938289430538>';
            await interaction.reply(resp);
            return;
        case 'emoji_sus':
            resp = '<:sus:925809579558830140>';
            await interaction.reply(resp);
            return;
        case 'emoji_facehoof':
            resp = '<:twiFacehoof:643785797723488256>';
            await interaction.reply(resp);
            return;
        case 'emoji_uno':
            resp = '<:reverse:685486822104891481>';
            await interaction.reply(resp);
            return;
        case 'emoji_fbi':
            resp = '<:fbi:932325031718047864>';
            await interaction.reply(resp);
            return;
        case 'emoji_fhd':
            resp = '<:fhd:951388464706891786>';
            await interaction.reply(resp);
            return;
        case 'emoji_uhd':
            resp = '<:uhd:951388856073211924>';
            await interaction.reply(resp);
            return;
        case 'emoji_deadchat':
            resp = '<:deadchat:686572384819478531>';
            await interaction.reply(resp);
            return;
        }

        switch (puff) {
        case 'emoji_buldiga':
            resp = '<:pwBuldiga:630872804505681931>';
            await interaction.reply(resp);
            return;
        }

        await interaction.reply({ content: 'Emoji is not selected or does not exist. Use one of the categories to select emoji', ephemeral: true });
    },
};