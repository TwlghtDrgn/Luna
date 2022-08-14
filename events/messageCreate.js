module.exports = {
    name: 'messageCreate',
    execute(message) {
        if (message.author.bot) return;
        if (!(/луна|лулу|луняш|лу, ?/giu.test(message.content))) return;
        console.log(`${message.author.tag} sent a message to me: ${message.content}`);
    },
};