package xyz.menherabot.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.menherabot.Constants;

import javax.annotation.Nonnull;
import java.util.Objects;

public class Ready extends ListenerAdapter {
    public void onReady(@Nonnull ReadyEvent event) {
        Logger logger = LoggerFactory.getLogger(Ready.class);
        logger.info("Bot online para {} servidor", event.getGuildTotalCount());

        JDA jda = event.getJDA();

        CommandListUpdateAction commands = Objects.requireNonNull(jda.getGuildById(Constants.GUILD_ID)).updateCommands();

        commands.addCommands(
                Commands.slash("status", "Escolha entre receber ou não atualizaçõs dos Status da Menhera"),
                Commands.slash("beta", "Escolha entre receber ou não notificações das atualizações da Beta da Menhera"),
                Commands.slash("atualizações",
                        "Escolha entre receber ou não notificações de atualizações lançadas da Menhera"),
                Commands.slash("info", "Informações sobre a Amandinha"));

        commands.queue();
    }
}
