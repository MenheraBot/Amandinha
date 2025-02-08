package xyz.menherabot.commands;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import xyz.menherabot.Constants;

import java.util.Objects;

public class NotifyCommand {
  public void execute(@Nonnull SlashCommandInteractionEvent e) {

    Role role = Objects.requireNonNull(e.getGuild()).getRoleById(Constants.NOTIFY_ROLE);

    if (Objects.requireNonNull(e.getMember()).getRoles().contains(role)) {
        assert role != null;
        e.getGuild().removeRoleFromMember(e.getMember(), role).queue();
      e.reply("Como desejar! Você não será mais notificado das atualizações da Menhera!").queue();
      return;
    }

      assert role != null;
      e.getGuild().addRoleToMember(e.getUser(), role).queue();
    e.reply("Feitoria! Você será notificado das atualizações da Menhera").queue();
    return;
  }
}
