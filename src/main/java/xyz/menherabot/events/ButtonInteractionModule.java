package xyz.menherabot.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import xyz.menherabot.Constants;
import xyz.menherabot.MessageCollector;
import xyz.menherabot.Statuspage;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ButtonInteractionModule extends ListenerAdapter {
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent e) {
        if (e.getUser().getIdLong() != Constants.OWNER_ID)
            return;

        if (e.getChannel().getIdLong() == Constants.ALERTS_CHANNEL) {
            String[] stripped = Objects.requireNonNull(e.getButton().getId()).split("-");

            String status = stripped[0];
            String service = stripped[1];

            e.deferEdit().queue();

            Statuspage.UpdateComponentState(Statuspage.GetComponentId(service), status);
            return;
        }

        switch (e.getComponentId()) {
            case "OK": {
                TextChannel channel = Objects.requireNonNull(e.getGuild()).getTextChannelById(Constants.ACCEPTED_CHANNEL);
                MessageEmbed oldEmbed = e.getMessage().getEmbeds().get(0);
                EmbedBuilder newEmbed = new EmbedBuilder()
                        .setTitle("Sugestão aceita!")
                        .setColor(Color.GREEN)
                        .setFooter(Objects.requireNonNull(oldEmbed.getFooter()).getText())
                        .setDescription(oldEmbed.getDescription());

                if (oldEmbed.getThumbnail() != null && oldEmbed.getThumbnail().getUrl() != null)
                    newEmbed.setThumbnail(oldEmbed.getThumbnail().getUrl());

                if (oldEmbed.getAuthor() != null && oldEmbed.getAuthor().getName() != null)
                    newEmbed.setAuthor(oldEmbed.getAuthor().getName(), oldEmbed.getAuthor().getIconUrl());

                assert channel != null;
                channel.sendMessageEmbeds(newEmbed.build()).queue();
                e.getMessage().delete().queue();
                break;
            }
            case "FILA": {
                TextChannel channel = Objects.requireNonNull(e.getGuild()).getTextChannelById(Constants.IN_QUEUE_CHANNEL);
                MessageEmbed oldEmbed = e.getMessage().getEmbeds().get(0);
                EmbedBuilder newEmbed = new EmbedBuilder()
                        .setTitle("Lux está fazendo esta sugestão!")
                        .setColor(Color.YELLOW)
                        .setFooter(Objects.requireNonNull(oldEmbed.getFooter()).getText())
                        .setDescription(oldEmbed.getDescription());

                if (oldEmbed.getThumbnail() != null && oldEmbed.getThumbnail().getUrl() != null)
                    newEmbed.setThumbnail(oldEmbed.getThumbnail().getUrl());

                if (oldEmbed.getAuthor() != null && oldEmbed.getAuthor().getName() != null)
                    newEmbed.setAuthor(oldEmbed.getAuthor().getName(), oldEmbed.getAuthor().getIconUrl());

                assert channel != null;
                channel.sendMessageEmbeds(newEmbed.build()).setActionRow(
                                net.dv8tion.jda.api.interactions.components.buttons.Button.success("OK", "Aceitar")
                                        .withEmoji(Emoji.fromUnicode("✅")),
                                Button.danger("NO", "Negar")
                                        .withEmoji(Emoji.fromUnicode("❌")))
                        .queue();

                e.getMessage().delete().queue();
                break;
            }
            case "NO": {
                MessageEmbed oldEmbed = e.getMessage().getEmbeds().get(0);
                EmbedBuilder newEmbed = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setFooter(Objects.requireNonNull(oldEmbed.getFooter()).getText())
                        .setDescription(oldEmbed.getDescription());

                if (oldEmbed.getThumbnail() != null && oldEmbed.getThumbnail().getUrl() != null)
                    newEmbed.setThumbnail(oldEmbed.getThumbnail().getUrl());

                if (oldEmbed.getAuthor() != null && oldEmbed.getAuthor().getName() != null)
                    newEmbed.setAuthor("A " + oldEmbed.getAuthor().getName() +
                            " Foi Negada", oldEmbed.getAuthor().getIconUrl());

                e.getJDA().addEventListener(new MessageCollector(e.getMessage().getIdLong(), newEmbed));

                e.reply("Qual o motivo para recusar essa reação?").complete().deleteOriginal().queueAfter(30,
                        TimeUnit.SECONDS);
                break;
            }
        }
    }
}
