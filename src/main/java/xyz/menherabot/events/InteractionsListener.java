package xyz.menherabot.events;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.menherabot.commands.BetaCommand;
import xyz.menherabot.commands.InfoCommand;
import xyz.menherabot.commands.NotifyCommand;
import xyz.menherabot.commands.StatusCommand;

import javax.annotation.Nonnull;

public class InteractionsListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent e) {
        String commandName = e.getName().split(" ")[0];

        if (!e.isFromGuild()) {
            e.reply("Este comando só pode ser executado no meu servidor de suporte").setEphemeral(true).queue();
        }

        switch (commandName) {
            case "beta" -> new BetaCommand().execute(e);
            case "atualizações" -> new NotifyCommand().execute(e);
            case "status" -> new StatusCommand().execute(e);
            case "info" -> new InfoCommand().execute(e);
        }
    }
}
