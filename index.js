/* eslint-disable no-unused-vars */
// Init stuff
const fs = require('node:fs');
const path = require('node:path');
const wait = require('node:timers/promises').setTimeout;
const si = require('systeminformation');
const os = require('node:os');
const ascii = require('ascii-text-generator');
const settings = { method: 'Get' };
const fetch = require('node-fetch');
const radio = 'https://radio.twilightdev.ru/api/nowplaying/1';
const eco = 'https://ci.twilightdev.ru/job/Eco-GriefDefender/api/json';

// Init DiscordJS
const { Client, GatewayIntentBits, Collection, InteractionType, version, ActionRowBuilder, ButtonBuilder, ButtonStyle } = require('discord.js');
const { token, ver } = require('./config.json');
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
const embed = require('./embeds');

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

// Commands
client.commands = new Collection();

const commandsPath = path.join(__dirname, 'commands');
const commandFiles = fs.readdirSync(commandsPath).filter(file => file.endsWith('.js'));

for (const file of commandFiles) {
    const filePath = path.join(commandsPath, file);
    const command = require(filePath);
    client.commands.set(command.data.name, command);
}

// Events
const eventsPath = path.join(__dirname, 'events');
const eventFiles = fs.readdirSync(eventsPath).filter(file => file.endsWith('.js'));

for (const file of eventFiles) {
    const filePath = path.join(eventsPath, file);
    const event = require(filePath);
    if (event.once) {
        client.once(event.name, (...args) => event.execute(...args, logger, client));
    } else {
        client.on(event.name, (...args) => event.execute(...args, logger, client));
    }
}

// Message replier
client.on('messageCreate', async msg => {
    if (msg.author.bot) return;
    if (!(/луна|лулу|луняш|лу, ?/giu.test(msg.content))) return;

    try {
        if (msg.content.includes('eco')) {
            await fetch(eco, settings)
                .then(res => res.json())
                .then((json) => {
                    msg.reply(`Последний успешный билд Eco: ${json.lastSuccessfulBuild.url}`);
                });
        } else if (/предыдущий|предыдущим|до этого|прошлый ?/giu.test(msg.content)) {
            await fetch(radio, settings)
                .then(res => res.json())
                .then((json) => {
                    msg.reply(`Предыдущий трек: \`${json.song_history[0].song.text}\``);
                });
        } else if (/далее|дальше|следующим|следующий ?/giu.test(msg.content)) {
            await fetch(radio, settings)
                .then(res => res.json())
                .then((json) => {
                    msg.reply(`Следующий трек: \`${json.playing_next.song.text}\``);
                });
        } else if (/о себе|о тебе ?/giu.test(msg.content)) {
            await msg.reply(`\`\`\`fix\nЯ - Луна.\nИграю радио, двигаю луну, когда в настроении то чатюсь с людьми, брожу по снам и все такое.\nМоя версия ${ver}, а версия API: ${version}.\nТакже, я нахожусь в ${client.guilds.cache.size} гильдиях и знаю около ${client.users.cache.size} пользователей.\nЕсли хочешь узнать информацию о системе, напиши \`Луна, статус\`\n\`\`\``);
        } else if (/статус|сводка|статы ?/giu.test(msg.content)) {
            await msg.reply(`\`\`\`md\n${await ascii('Luna', '2')}\n\n<Version ${ver}>\t<Discord.JS ${version}>\t<Bot_Uptime ${Math.floor(process.uptime())} seconds>\t<System_Uptime ${os.uptime()} seconds>\n<CPU ${(await si.cpu()).brand}>\n<RAM ${(await si.mem()).total / 1024 / 1024 / 1024} GB>\n<Temp: ${(await si.cpuTemperature()).main}>\n<OS ${(await si.osInfo()).distro}>\n<Kernel ${(await si.osInfo()).kernel}>\n\`\`\``);
        } else if ((/создать репорты ?/giu.test(msg.content)) && (msg.author.id === '339488218523238410')) {
            const reportsRow = new ActionRowBuilder()
                .addComponents(
                    new ButtonBuilder()
                        .setCustomId('bugReportBtn')
                        .setLabel('Отправить баг-репорт')
                        .setStyle(ButtonStyle.Success),
                )
                .addComponents(
                    new ButtonBuilder()
                        .setCustomId('playerReportBtn')
                        .setLabel('Отправить репорт на игрока')
                        .setStyle(ButtonStyle.Danger),
                );

            await msg.channel.send({ embeds: [embed.notImplementedEmbed], components: [reportsRow] });
        }
    } catch (error) {
        await wait(1000);
        logger.error(`!! > ${error}`);
        msg.reply({ embeds: [embed.errorEmbed], ephemeral: true });
    }
});

// Command runner
client.on('interactionCreate', async interaction => {
    if (!interaction.isChatInputCommand()) return;
    if (interaction.type === InteractionType.ModalSubmit) return;
    logger.info(`User "${interaction.user.tag}" at guild "${interaction.guild.name}" in channel "#${interaction.channel.name}" triggered a command: ${interaction}`);

    const command = client.commands.get(interaction.commandName);

    if (!command) return;

    try {
        await command.execute(interaction, client, logger, embed);
    } catch (error) {
        await wait(1000);
        logger.error(`!! > ${error}`);
        await interaction.reply({ embeds: [embed.errorEmbed], ephemeral: true });
    }
});

client.login(token);
