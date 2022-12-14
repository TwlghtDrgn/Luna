package net.twlghtdrgn.luna.Commands.Fun;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.twlghtdrgn.luna.Luna;

@SuppressWarnings("ConstantConditions")
public class EmojiCommand extends ListenerAdapter {
    private final Luna luna;

    public EmojiCommand(Luna luna) {
        this.luna = luna;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.isFromGuild()) return;
        if (!event.getName().equals("emoji")) return;
        String twlght = "null", puff = "null", fs = "null", val = "null";

        if (event.getOption("twlghtdrgn") != null) twlght = event.getOption("twlghtdrgn").getAsString();
        if (event.getOption("puffers") != null) puff = event.getOption("puffers").getAsString();
        if (event.getOption("fs") != null) fs = event.getOption("fs").getAsString();

        if (!twlght.equals("null")) {
            switch (twlght) {
                case ("emoji_derp") -> val = "<:derpyShrug_0:643766689376501760>";
                case ("emoji_lulu") -> val = "<:luluShrug:711862558826692660>";
                case ("emoji_tia") -> val = "<:tiaShrug:643786229548056576>";
                case ("emoji_repel") -> val = "<:repel:911605102383595540>";
                case ("emoji_lyra") -> val = "<:lyraThinking:860056938289430538>";
                case ("emoji_sus") -> val = "<:sus:925809579558830140>";
                case ("emoji_facehoof") -> val = "<:twiFacehoof:643785797723488256>";
                case ("emoji_uno") -> val = "<:reverse:685486822104891481>";
                case ("emoji_fbi") -> val = "<:fbi:932325031718047864>";
                case ("emoji_fhd") -> val = "<:fhd:951388464706891786>";
                case ("emoji_uhd") -> val = "<:uhd:951388856073211924>";
                case ("emoji_deadchat") -> val = "<:deadchat:686572384819478531>";
            }
            event.reply(val).queue();
            return;
        }

        if (!puff.equals("null")) {
            switch (puff) {
                case ("emoji_buldiga") -> val = "<:pwBuldiga:630872804505681931>";
            }
            event.reply(val).queue();
            return;
        }

        if (!fs.equals("null")) {
            switch (fs) {
                case ("emoji_igrosmile") -> val = "<:FSigrolev:1033084051751452702>";
                case ("emoji_hunterstare") -> val = "<:FSangry:1038474608237608970>";
                case ("emoji_drog") -> val = "<:FSdrog:1040327306310340699>";
                case ("emoji_moderator") -> val = "<:FSmoderator:1034129005726670909>";
                case ("emoji_hunterscream") -> val = "<:FShunter:1012034949722947604>";
            }
            event.reply(val).queue();
            return;
        }

        event.reply("Emoji is not selected or does not exist. Use one of the categories to select emoji")
                .setEphemeral(true).queue();
    }
}
