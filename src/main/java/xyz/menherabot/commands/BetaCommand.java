package xyz.menherabot.commands;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import xyz.menherabot.Constants;

import java.util.Objects;

public class BetaCommand {
  public void execute(@Nonnull SlashCommandInteractionEvent e) {

    Role role = Objects.requireNonNull(e.getGuild()).getRoleById(Constants.BETA_ROLE);

    if (Objects.requireNonNull(e.getMember()).getRoles().contains(role)) {
        assert role != null;
        e.getGuild().removeRoleFromMember(e.getMember(), role).queue();
      e.reply("Perfeitamente, você perdeu o acesso às atualizações da Beta").queue();
      return;
    }

      assert role != null;
      e.getGuild().addRoleToMember(e.getUser(), role).queue();
    e.reply("DAliiii. Você agora será notificado das atualizações da beta. Tudo isso acontece no <#852197292589187094>")
        .queue();
  }
}
