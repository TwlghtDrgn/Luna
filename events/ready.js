const { ActivityType } = require('discord.js');

module.exports = {
    name: 'ready',
    once: true,
    execute(client) {
        console.log(`[Luna] > Logged in as ${client.user.tag}`);
        client.user.setStatus('idle');
        client.user.setActivity('with your dreams', { type: ActivityType.Playing });
        console.log('[Luna] > Successfully awake!');
    },
};