package xyz.menherabot.events;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import xyz.menherabot.Constants;

import javax.annotation.Nonnull;

public class Ready extends ListenerAdapter {
    public void onReady(@Nonnull ReadyEvent event) {
        System.out.println("Bot online para " + event.getGuildTotalCount() + " servidor");

        CommandListUpdateAction commands = event.getJDA().getGuildById(Constants.GUILD_ID).updateCommands();

        commands.addCommands(
                Commands.slash("status", "Escolha entre receber ou não atualizaçõs dos Status da Menhera"),
                Commands.slash("beta", "Escolha entre receber ou não notificações das atualizações da Beta da Menhera"),
                Commands.slash("atualizações",
                        "Escolha entre receber ou não notificações de atualizações lançadas da Menhera"),
                Commands.slash("info", "Informações sobre a Amandinha"));

        commands.queue();
    }
}
