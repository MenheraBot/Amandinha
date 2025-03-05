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
import java.util.regex.Matcher;
import javax.annotation.Nonnull;
import java.awt.*;
import okhttp3.*; 
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ButtonInteractionModule extends ListenerAdapter {
    public static void sendPostRequest(String userId) throws IOException {
        String json = "{\"userId\":\"" + userId + "\"}";

        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url("https://api.menhera.com/main/suggestion")
                .addHeader("Authorization", System.getenv("MENHERA_API_TOKEN"))
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Success in suggestion: " + response.body().string());
            } else {
                System.out.println("Error in suggestion: " + response.code());
            }
        }
    }
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

                String oldUserId = oldEmbed.getFooter().getText();

                Pattern pattern = Pattern.compile("ID do Usuário: (\\d+)");
                Matcher matcher = pattern.matcher(oldUserId);

                String userId = null;
                if (matcher.find()) {
                    userId = matcher.group(1);

                    try {
                        sendPostRequest(userId);
                    } catch (IOException err) {
                        err.printStackTrace();
                    }
                }

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
