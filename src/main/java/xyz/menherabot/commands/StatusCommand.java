package xyz.menherabot.commands;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import xyz.menherabot.Constants;

import java.util.Objects;

public class StatusCommand {
  public void execute(@Nonnull SlashCommandInteractionEvent e) {

    Role role = Objects.requireNonNull(e.getGuild()).getRoleById(Constants.STATUS_ROLE);

    if (Objects.requireNonNull(e.getMember()).getRoles().contains(role)) {
        assert role != null;
        e.getGuild().removeRoleFromMember(e.getMember(), role).queue();
      e.reply("Como desejar! Você não será mais notificado dos status da Menhera!").queue();
      return;
    }

      assert role != null;
      e.getGuild().addRoleToMember(e.getUser(), role).queue();
    e.reply("Feitoria! Você será notificado dos status da Menhera").queue();

  }
}
