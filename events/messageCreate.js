module.exports = {
    name: 'messageCreate',
    execute(msg, logger) {
        if (msg.author.bot) return;
        if (!(/луна|лулу|луняш|лу, ?/giu.test(msg.content))) return;
        logger.info(`${msg.author.tag} sent a message to me: ${msg.content}`);
    },
};