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

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        System.out.println("Bot online para " + event.getGuildTotalCount() + " servidor");

        CommandListUpdateAction commands = event.getJDA().getGuildById(Constants.GUILD_ID).updateCommands();

        commands.addCommands(
            Commands.slash("status", "Escolha entre receber ou n??o atualiza????s dos Status da Menhera"),
            Commands.slash("beta", "Escolha entre receber ou n??o notifica????es das atualiza????es da Beta da Menhera"),
            Commands.slash("atualiza????es", "Escolha entre receber ou n??o notifica????es de atualiza????es lan??adas da Menhera"),
            Commands.slash("info", "Informa????es sobre a Amandinha")
            );

        commands.queue();
    }

    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent e) {
        if (e.getUser().getIdLong() != Constants.OWNER_ID) return;

        switch(e.getComponentId()){
            case "OK": {
                TextChannel channel = e.getGuild().getTextChannelById(Constants.ACCEPTED_CHANNEL);
                MessageEmbed oldEmbed = e.getMessage().getEmbeds().get(0);
                EmbedBuilder newEmbed = new EmbedBuilder()
                        .setTitle("Sugest??o aceita!")
                        .setColor(Color.GREEN)
                        .setFooter(oldEmbed.getFooter().getText())
                        .setDescription(oldEmbed.getDescription());

                if(oldEmbed.getThumbnail() != null && oldEmbed.getThumbnail().getUrl() != null)
                     newEmbed.setThumbnail(oldEmbed.getThumbnail().getUrl());

                if(oldEmbed.getAuthor() != null && oldEmbed.getAuthor().getName() != null)
                    newEmbed.setAuthor(oldEmbed.getAuthor().getName(), oldEmbed.getAuthor().getIconUrl());

        
                channel.sendMessageEmbeds(newEmbed.build()).queue();
                e.getMessage().delete().queue();
                break;
            }
            case "FILA": {
                TextChannel channel = e.getGuild().getTextChannelById(Constants.IN_QUEUE_CHANNEL);
                MessageEmbed oldEmbed = e.getMessage().getEmbeds().get(0);
                EmbedBuilder newEmbed = new EmbedBuilder()
                        .setTitle("Lux est?? fazendo esta sugest??o!")
                        .setColor(Color.YELLOW)
                        .setFooter(oldEmbed.getFooter().getText())
                        .setDescription(oldEmbed.getDescription());

                        if(oldEmbed.getThumbnail() != null && oldEmbed.getThumbnail().getUrl() != null)
                        newEmbed.setThumbnail(oldEmbed.getThumbnail().getUrl());
   
                   if(oldEmbed.getAuthor() != null && oldEmbed.getAuthor().getName() != null)
                       newEmbed.setAuthor(oldEmbed.getAuthor().getName(), oldEmbed.getAuthor().getIconUrl());

                channel.sendMessageEmbeds(newEmbed.build()).setActionRow(
                    Button.success("OK", "Aceitar")
                        .withEmoji(Emoji.fromUnicode("???")),
                    Button.danger("NO", "Negar")
                        .withEmoji(Emoji.fromUnicode("???"))
                    ).queue();

                e.getMessage().delete().queue();
                break;
            }
            case "NO": {
                MessageEmbed oldEmbed = e.getMessage().getEmbeds().get(0);
                EmbedBuilder newEmbed = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setFooter(oldEmbed.getFooter().getText())
                        .setDescription(oldEmbed.getDescription());

                        if(oldEmbed.getThumbnail() != null && oldEmbed.getThumbnail().getUrl() != null)
                        newEmbed.setThumbnail(oldEmbed.getThumbnail().getUrl());
   
                   if(oldEmbed.getAuthor() != null && oldEmbed.getAuthor().getName() != null)
                       newEmbed.setAuthor(new StringBuilder("A ").append(oldEmbed.getAuthor().getName()).append(" Foi Negada").toString(), oldEmbed.getAuthor().getIconUrl());

                e.getJDA().addEventListener(new MessageCollector(e.getMessage().getIdLong(), newEmbed));

                e.reply("Qual o motivo para recusar essa rea????o?").complete().deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                break;
            }
        }
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;
        if (!e.isFromGuild()) return;

        if (e.getMessage().getContentRaw().toLowerCase().startsWith("m!")) {
            String replyMessage = String.format("Oii %s, a Menhera n??o usa mais comandos de mensagem!\nUse os comandos slash. Eles come??am com `/`. Ao digitar, uma janela aparecer?? com todos comandos existentes, escolha o que voc?? quer, e parte pro abra??o >..<", e.getAuthor().getName());
            e.getMessage().reply(replyMessage).queue();
            return;
        }

        if(e.getChannel().getIdLong() == Constants.SUGGEST_CHANNEL) {
            EmbedBuilder embed = new EmbedBuilder()
            .setDescription("**"+ e.getMessage().getContentRaw() +"**")
            .setColor(new Color(new Random().nextInt(0xffffff + 1)))
            .setThumbnail(e.getAuthor().getAvatarUrl())
            .setFooter(new StringBuilder("ID do Usu??rio: ").append(e.getAuthor().getId()).toString())
            .setTimestamp(Instant.now())
            .setAuthor(new StringBuilder("Sugest??o de ").append(e.getAuthor().getAsTag()).toString(), e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());

            e.getGuild().getTextChannelById(Constants.WAITING_CHANNEL)
            .sendMessageEmbeds(embed.build())
            .setActionRow(
                Button.success("OK", "Aceitar"),
                Button.danger("NO", "Negar"),
                Button.primary("FILA", "Fila")
                    .withEmoji(Emoji.fromUnicode("????")
                )
            ).queue();

            e.getMessage().delete().queue();

            e.getChannel().sendMessage(
                new StringBuilder("Obrigada por me enviar uma sugest??o ")
                .append(e.getAuthor().getAsMention())
                .append("! Minha dona j?? possui conhecimento dela, e vai averiguar o mais r??pido poss??vel. Beijinhos >.<")
                .toString()
            ).complete().delete().queueAfter(10, TimeUnit.SECONDS);
            return;
        }
    }
}
