package xyz.menherabot.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import xyz.menherabot.Constants;
import xyz.menherabot.Statuspage;

import javax.annotation.Nonnull;
import java.awt.*;
import java.time.Instant;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MessageReceive extends ListenerAdapter {
    public void onMessageReceived(@Nonnull MessageReceivedEvent e) {
        if (!e.isFromGuild())
            return;

        if (e.getChannel().getIdLong() == Constants.ALERTS_CHANNEL
                && e.getMessage().getContentRaw().contains("NOTIFY STATUSPAGE")) {
            Statuspage.CheckMessage(e.getMessage());
            e.getMessage().delete().queue();
            return;
        }

        if (e.getChannel().getIdLong() == Constants.SUGGEST_CHANNEL) {
            if (e.getAuthor().isBot() && e.getAuthor().getIdLong() != Constants.MENHERA_BOT_ID)
                return;

            EmbedBuilder embed = new EmbedBuilder()
                    .setDescription("**" + e.getMessage().getContentRaw() + "**")
                    .setColor(new Color(new Random().nextInt(0xffffff + 1)))
                    .setThumbnail(e.getAuthor().getAvatarUrl())
                    .setFooter("ID do Usuário: " + e.getAuthor().getId())
                    .setTimestamp(Instant.now())
                    .setAuthor("Sugestão de " + e.getAuthor().getAsTag(),
                            e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());

            Objects.requireNonNull(e.getGuild().getTextChannelById(Constants.WAITING_CHANNEL))
                    .sendMessageEmbeds(embed.build())
                    .setActionRow(
                            Button.success("OK", "Aceitar"),
                            Button.danger("NO", "Negar"),
                            Button.primary("FILA", "Fila")
                                    .withEmoji(Emoji.fromUnicode("🟡")))
                    .queue();

            e.getMessage().delete().queue();

            if (e.getAuthor().getIdLong() != Constants.MENHERA_BOT_ID) {
                e.getChannel().sendMessage(
                                "Obrigada por me enviar uma sugestão " +
                                        e.getAuthor().getAsMention() +
                                        "! Minha dona já possui conhecimento dela, e vai averiguar o mais rápido possível. Beijinhos >.<")
                        .complete().delete().queueAfter(10, TimeUnit.SECONDS);
            }
            return;
        }

        if (e.getAuthor().isBot())
            return;

        String content = e.getMessage().getContentRaw().toLowerCase();

        if (content.startsWith("m!")) {
            String replyMessage = String.format(
                    "Oii %s, a Menhera não usa mais comandos de mensagem!\nUse os comandos slash. Eles começam com `/`. Ao digitar, uma janela aparecerá com todos comandos existentes, escolha o que você quer, e parte pro abraço >..<",
                    e.getAuthor().getName());
            e.getMessage().reply(replyMessage).queue();
            return;
        }

        if (content.contains("como")
                && (content.contains("moeda") || content.contains("estrel") || content.contains("dinheiro"))) {
            e.getMessage()
                    .reply("Você quer saber como ganhar **estrelinhas** :star:, a moeda da Menhera?\nVeja como nessa mensagem -> https://canary.discord.com/channels/717061688460967988/773973549386825759/1093721660001103952")
                    .setActionRow(Button.link(
                            "https://discord.com/channels/717061688460967988/773973549386825759/1093721660001103952",
                            "Clique Para ver a Mensagem"))
                    .queue();

            return;
        }

        if (content.contains("^^")) {
            Random rand = new Random();
            int randomNumber = rand.nextInt(5) + 1;

            if (randomNumber == 3) {
                e.getMessage().addReaction(":eto:782022370231582721").queue();
            }
        }
    }
}
