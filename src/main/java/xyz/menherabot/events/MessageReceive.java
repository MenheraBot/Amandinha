package xyz.menherabot.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import xyz.menherabot.Constants;

import javax.annotation.Nonnull;
import java.awt.*;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MessageReceive extends ListenerAdapter {
    public void onMessageReceived(@Nonnull MessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;
        if (!e.isFromGuild()) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith("m!")) {
            String replyMessage = String.format("Oii %s, a Menhera não usa mais comandos de mensagem!\nUse os comandos slash. Eles começam com `/`. Ao digitar, uma janela aparecerá com todos comandos existentes, escolha o que você quer, e parte pro abraço >..<", e.getAuthor().getName());
            e.getMessage().reply(replyMessage).queue();
            return;
        }

        if(e.getChannel().getIdLong() == Constants.SUGGEST_CHANNEL) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setDescription("**"+ e.getMessage().getContentRaw() +"**")
                    .setColor(new Color(new Random().nextInt(0xffffff + 1)))
                    .setThumbnail(e.getAuthor().getAvatarUrl())
                    .setFooter(new StringBuilder("ID do Usuário: ").append(e.getAuthor().getId()).toString())
                    .setTimestamp(Instant.now())
                    .setAuthor(new StringBuilder("Sugestão de ").append(e.getAuthor().getAsTag()).toString(), e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());

            e.getGuild().getTextChannelById(Constants.WAITING_CHANNEL)
                    .sendMessageEmbeds(embed.build())
                    .setActionRow(
                            net.dv8tion.jda.api.interactions.components.buttons.Button.success("OK", "Aceitar"),
                            net.dv8tion.jda.api.interactions.components.buttons.Button.danger("NO", "Negar"),
                            Button.primary("FILA", "Fila")
                                    .withEmoji(Emoji.fromUnicode("🟡")
                                    )
                    ).queue();

            e.getMessage().delete().queue();

            e.getChannel().sendMessage(
                    new StringBuilder("Obrigada por me enviar uma sugestão ")
                            .append(e.getAuthor().getAsMention())
                            .append("! Minha dona já possui conhecimento dela, e vai averiguar o mais rápido possível. Beijinhos >.<")
                            .toString()
            ).complete().delete().queueAfter(10, TimeUnit.SECONDS);
            return;
        }
    }
}
