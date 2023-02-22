package xyz.menherabot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import xyz.menherabot.commands.BetaCommand;
import xyz.menherabot.commands.InfoCommand;
import xyz.menherabot.commands.NotifyCommand;
import xyz.menherabot.commands.StatusCommand;

import java.awt.Color;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

public class Amandinha extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        JDABuilder.createLight(System.getenv("TOKEN"), EnumSet.noneOf(GatewayIntent.class))
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.playing("Averiguando meu Servidor de suporte"))
                .enableIntents(EnumSet.of(GatewayIntent.GUILD_MESSAGES))
                .addEventListeners(
                    new Amandinha(),
                    new BetaCommand(),
                    new NotifyCommand(),
                    new StatusCommand(),
                    new InfoCommand()
                )
                .build();
    }
}
