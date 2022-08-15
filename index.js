/* eslint-disable no-unused-vars */
const fs = require('node:fs');
const os = require('node:os');
const path = require('node:path');
const wait = require('node:timers/promises').setTimeout;
const { Client, GatewayIntentBits, Collection, EmbedBuilder, ActivityType, version } = require('discord.js');
const { token, ver } = require('./config.json');
const fetch = require('node-fetch');
const si = require('systeminformation');
const ascii = require('ascii-text-generator');
const settings = { method: 'Get' };
const radio = 'https://radio.twilightdev.ru/api/nowplaying/1';
const eco = 'https://ci.twilightdev.ru/job/Eco-GriefDefender/api/json';

// Logger

const { createLogger, format, transports } = require('winston');
const { combine, timestamp, simple } = format;

const logger = createLogger({
    transports: [
        new transports.Console(),
        new transports.File({ filename: 'luna.log' }),
    ],
    exitOnError: false,
    format: combine(
        timestamp(),
        simple(),
    ),
});

const client = new Client({ intents: [
    GatewayIntentBits.DirectMessageReactions,
    GatewayIntentBits.DirectMessageTyping,
    GatewayIntentBits.DirectMessages,
    GatewayIntentBits.GuildBans,
    GatewayIntentBits.GuildEmojisAndStickers,
    GatewayIntentBits.GuildIntegrations,
    GatewayIntentBits.GuildInvites,
    GatewayIntentBits.GuildMembers,
    GatewayIntentBits.GuildMessageReactions,
    GatewayIntentBits.GuildMessageTyping,
    GatewayIntentBits.GuildMessages,
    GatewayIntentBits.GuildPresences,
    GatewayIntentBits.GuildScheduledEvents,
    GatewayIntentBits.GuildVoiceStates,
    GatewayIntentBits.GuildWebhooks,
    GatewayIntentBits.Guilds,
    GatewayIntentBits.MessageContent,
] });

client.commands = new Collection();

const commandsPath = path.join(__dirname, 'commands');

const commandFiles = fs.readdirSync(commandsPath).filter(file => file.endsWith('.js'));

for (const file of commandFiles) {
    const filePath = path.join(commandsPath, file);
    const command = require(filePath);
    client.commands.set(command.data.name, command);
}


client.on('messageCreate', async msg => {
    if (msg.author.bot) return;
    if (!(/луна|лулу|луняш|лу, ?/giu.test(msg.content))) return;
    logger.info(`${msg.author.tag} sent a message to me: ${msg.content}`);

    try {
        if (msg.content.includes('eco')) {
            await fetch(eco, settings)
                .then(res => res.json())
                .then((json) => {
                    msg.reply(`Последний успешный билд Eco: ${json.lastSuccessfulBuild.url}`);
                });
            return;
        } else if (/предыдущий|предыдущим|до этого|прошлый ?/giu.test(msg.content)) {
            await fetch(radio, settings)
                .then(res => res.json())
                .then((json) => {
                    msg.reply(`Предыдущий трек: \`${json.song_history[0].song.text}\``);
                });
            return;
        } else if (/далее|дальше|следующим|следующий ?/giu.test(msg.content)) {
            await fetch(radio, settings)
                .then(res => res.json())
                .then((json) => {
                    msg.reply(`Следующий трек: \`${json.playing_next.song.text}\``);
                });
            return;
        } else if (/о себе|о тебе ?/giu.test(msg.content)) {
            await msg.reply(`\`\`\`fix\nЯ - Луна.\nИграю радио, двигаю луну, когда в настроении то чатюсь с людьми, брожу по снам и все такое.\nМоя версия ${ver}, а версия API: ${version}.\nТакже, я нахожусь в ${client.guilds.cache.size} гильдиях и знаю около ${client.users.cache.size} пользователей.\nЕсли хочешь узнать информацию о системе, напиши \`Луна, статус\`\n\`\`\``);
        } else if (/статус|сводка|статы ?/giu.test(msg.content)) {
            await msg.reply(`\`\`\`md\n${await ascii('Luna', '2')}\n\n<Version ${ver}>\t<Discord.JS ${version}>\t<Bot_Uptime ${Math.floor(process.uptime())} seconds>\t<System_Uptime ${os.uptime()} seconds>\n<CPU ${(await si.cpu()).brand}>\n<RAM ${(await si.mem()).total / 1024 / 1024 / 1024} GB>\n<Temp: ${(await si.cpuTemperature()).main}>\n<OS ${(await si.osInfo()).distro}>\n<Kernel ${(await si.osInfo()).kernel}>\n\`\`\``);
        } else {
            await wait(Math.floor(Math.random() * 5) * 1000);
            await msg.channel.sendTyping();
            await wait(Math.floor(Math.random() * 3) * 1000);
            msg.reply('Известных команд не найдено <:luluShrug:711862558826692660>');
        }
    } catch (error) {
        await wait(1000);
        logger.error(`!! > ${error}`);
        msg.reply({ embeds: [errorEmbed], ephemeral: true });
    }
});

// Command runner
client.on('interactionCreate', async interaction => {
    if (!interaction.isChatInputCommand()) return;

    const command = client.commands.get(interaction.commandName);

    if (!command) return;

    try {
        await interaction.deferReply();
        await wait(1000);
        await command.execute(interaction, client, logger);
    } catch (error) {
        await wait(1000);
        logger.error(`!! > ${error}`);
        await interaction.editReply({ embeds: [errorEmbed], ephemeral: true });
    }
});

// Embeds
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
    .setDescription('Команда выполнена успешно')
    .setThumbnail('https://derpicdn.net/img/view/2014/11/23/770308.png')
    .setTimestamp();

const errorEmbed = new EmbedBuilder()
    .setColor('DarkVividPink')
    .setTitle('Что то пошло не так...')
    .setDescription('Судя по всему произошла ошибка: или у меня нет прав, или попалась ошибка в коде. Извини, и по возможности сообщи об этом <@339488218523238410>')
    .setTimestamp()
    .setThumbnail('https://cdn.discordapp.com/emojis/926033819675557949.webp?size=256&quality=lossless');

const noAccessEmbed = new EmbedBuilder()
    .setColor('DarkVividPink')
    .setTitle('Недостаточно прав')
    .setDescription('Ты забрел на запретную территорию. Беги.')
    .setTimestamp()
    .setThumbnail('https://cdn.discordapp.com/attachments/962656409898602537/1001951656650481854/403.png');

// So... Logger stuff goes here, I guess...

client.on('interactionCreate', async (interaction) => {
    logger.info(`User "${interaction.user.tag}" at guild "${interaction.guild.name}" in channel "#${interaction.channel.name}" triggered a command: ${interaction}`);
});

client.on('warn', async (warn) => {
    logger.warn(`!! > Warn: ${warn}`);
});

client.on('error', async (e) => {
    logger.error(`!! > Error: ${e}`);
});

client.on('ready', () => {
    logger.info('--------');
    logger.info(`[Luna] > Logged in as ${client.user.tag}`);
    client.user.setStatus('idle');
    client.user.setActivity('with your dreams', { type: ActivityType.Playing });
    logger.info('[Luna] > Successfully awake!');
});

process.on('uncaughtException', async (e) => {
    logger.error(`!! > Uncaught Exception: ${e}`);
});

process.on('unhandledRejection', async (reason, promise) => {
    logger.crit(`!! > Possibly Unhandled Rejection at: ${promise}, ${reason.message}`);
});

client.login(token);
