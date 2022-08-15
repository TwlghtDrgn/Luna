const request = require('node-fetch');
const {
    URL,
    URLSearchParams,
} = require('url');
const {
    chat,
} = require('../config.json');
const mainURL = new URL(chat.url);
const urlOptions = {
    bid: chat.brainID,
    key: chat.key,
    uid: null,
    msg: null,
};

const handleTalk = async (msg, logger) => {
    // msg.content = msg.content.replace(/^<@!?[0-9]{1,20}> ?/i, '');
    if (msg.content.length < 2) return;
    urlOptions.uid = msg.author.id;
    urlOptions.msg = msg.content;
    mainURL.search = new URLSearchParams(urlOptions).toString();
    try {
        let reply = await request(mainURL);
        if (reply) {
            reply = await reply.json();
            reply.cnt = reply.cnt.replace(/.U003C/g, '<');
            reply.cnt = reply.cnt.replace(/.U003E/g, '>');
            msg.reply({
                content: reply.cnt,
                allowedMentions: {
                    repliedUser: true,
                },
            });
        }
    } catch (e) {
        logger.error(e);
    }
};

module.exports = {
    handleTalk,
};
