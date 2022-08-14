module.exports = {
    name: 'interactionCreate',
    execute(interaction) {
        console.log(`User "${interaction.user.tag}" at guild "${interaction.guild.name}" in channel "#${interaction.channel.name}" triggered a command: ${interaction}`);
    },
};