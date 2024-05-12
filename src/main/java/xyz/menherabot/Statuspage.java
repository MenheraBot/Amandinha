package xyz.menherabot;

import java.io.IOException;
import net.dv8tion.jda.api.entities.Message;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

enum ComponentStates {
  operational, partial_outage, major_outage, degraded_performance, under_maintenance
};

public class Statuspage {
  private static OkHttpClient client = new OkHttpClient();
  public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

  public static void CheckMessage(Message message) {
    String rawContent = message.getContentRaw();

    String serviceName = getServiceName(rawContent);
    String componentId = Statuspage.GetComponentId(serviceName);

    if (rawContent.contains("UP")) {
      message.getChannel()
          .sendMessage("O serviço **" + serviceName + "** está pingando novamente, settar o status como operacional?")
          .setActionRow(
              net.dv8tion.jda.api.interactions.components.buttons.Button.success("operational-" + serviceName,
                  "Operacional"))
          .queue();
      return;
    }

    message.getChannel()
        .sendMessage("**SERVIÇO FORA DO AR**\n\n **" + serviceName
            + "** - Não está pingando. Estado settado como _Major Outage_\n\n<@" + Constants.OWNER_ID + ">")
        .setActionRow(
            net.dv8tion.jda.api.interactions.components.buttons.Button.primary("under_maintenance-" + serviceName,
                "Settar Manutenção"),
            net.dv8tion.jda.api.interactions.components.buttons.Button.danger("major_outage-" + serviceName,
                "Settar Major Outage"),
            net.dv8tion.jda.api.interactions.components.buttons.Button.secondary("partial_outage-" + serviceName,
                "Settar Partial Outage"),
            net.dv8tion.jda.api.interactions.components.buttons.Button.secondary("degraded_performance-" + serviceName,
                "Settar Degraded Performance"),
            net.dv8tion.jda.api.interactions.components.buttons.Button.link("https://menherabot.xyz/status",
                "Status Page"))
        .queue();

    sendStatusPageRequest(componentId, ComponentStates.major_outage);
  }

  public static void UpdateComponentState(String componentId, String stringetStatus) {
    sendStatusPageRequest(componentId, ComponentStates.valueOf(stringetStatus));
  }

  private static String getServiceName(String messageContent) {
    if (messageContent.contains("Vangogh"))
      return "VANGOGH";

    if (messageContent.contains("Album"))
      return "ALBUM";

    if (messageContent.contains("Api"))
      return "API";

    if (messageContent.contains("Website"))
      return "WEBSITE";

    if (messageContent.contains("Orchestrator"))
      return "ORCHESTRATOR";

    throw new Error("Unknown Service sent by UptimeRobot");
  }

  public static String GetComponentId(String service) {
    return System.getenv(service + "_COMPONENT_ID");
  }

  private static String requestJson(ComponentStates state) {
    return "{\"component\": {\"status\": \"" + state.name() + "\"}}";
  }

  private static void sendStatusPageRequest(String componentId, ComponentStates state) {
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    RequestBody body = RequestBody.create(requestJson(state), JSON);

    Request request = new Request.Builder()
        .url("https://api.statuspage.io/v1/pages/" + System.getenv("STATUSPAGE_PAGE_ID") + "/components/" + componentId)
        .addHeader("Authorization", "OAuth " + System.getenv("STATUSPAGE_TOKEN"))
        .patch(body)
        .build();

    try {
      client.newCall(request).execute();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
