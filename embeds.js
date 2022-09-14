const { EmbedBuilder } = require('discord.js');

const notImplementedEmbed = new EmbedBuilder()
    .setColor('#FF0000')
    .setTitle('Not Implemented')
    .setDescription('This functionality is not implemented yet.')
    .setThumbnail('https://cdn.discordapp.com/attachments/962656409898602537/974623596515328021/unknown.png')
    .setTimestamp();

const happyBDayEmbed = new EmbedBuilder()
    .setColor('#6a0dad')
    .setImage('https://derpicdn.net/img/download/2021/10/3/2715435.png')
    .setTimestamp();

const doneEmbed = new EmbedBuilder()
    .setColor('Green')
    .setTitle('Done')
    .setDescription('Задача выполнена 🙂')
    // .setThumbnail('https://derpicdn.net/img/view/2014/11/23/770308.png')
    .setTimestamp();

const errorEmbed = new EmbedBuilder()
    .setColor('DarkVividPink')
    .setTitle('Что то пошло не так...')
    .setDescription('Судя по всему произошла ошибка: или у меня нет прав, или попалась ошибка в коде. Рекомендую сообщить об этом проишествии <@339488218523238410>')
    .setTimestamp()
    .setThumbnail('https://cdn.discordapp.com/emojis/926033819675557949.webp?size=256&quality=lossless');

const noAccessEmbed = new EmbedBuilder()
    .setColor('DarkVividPink')
    .setTitle('Недостаточно прав')
    .setDescription('Ты забрел на запретную территорию. Беги.')
    .setTimestamp()
    .setThumbnail('https://cdn.discordapp.com/attachments/962656409898602537/1001951656650481854/403.png');

const templateEmbed = new EmbedBuilder()
    .setColor('#6a0dad')
    .setTimestamp();

module.exports = {
    notImplementedEmbed,
    happyBDayEmbed,
    doneEmbed,
    errorEmbed,
    noAccessEmbed,
    templateEmbed,
};