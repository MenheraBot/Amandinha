package xyz.menherabot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import xyz.menherabot.events.ButtonInteractionModule;
import xyz.menherabot.events.InteractionsListener;
import xyz.menherabot.events.MessageReceive;
import xyz.menherabot.events.Ready;

import java.util.EnumSet;

import javax.security.auth.login.LoginException;

public class Amandinha extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        JDABuilder.createLight(System.getenv("TOKEN"), EnumSet.noneOf(GatewayIntent.class))
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.playing("Averiguando meu Servidor de suporte"))
                .enableIntents(EnumSet.of(GatewayIntent.GUILD_MESSAGES))
                .addEventListeners(
                        new Amandinha(),
                        new InteractionsListener(),
                        new Ready(),
                        new MessageReceive(),
                        new ButtonInteractionModule())
                .build();
    }
}
