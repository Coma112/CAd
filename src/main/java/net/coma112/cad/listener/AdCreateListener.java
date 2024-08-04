package net.coma112.cad.listener;

import net.coma112.cad.enums.keys.ConfigKeys;
import net.coma112.cad.events.AdCreateEvent;
import net.coma112.cad.hooks.Webhook;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;
import java.net.URISyntaxException;

import static net.coma112.cad.hooks.Webhook.replacePlaceholdersAdCreate;

public class AdCreateListener implements Listener {
    @EventHandler
    public void onCreate(final AdCreateEvent event) throws IOException, NoSuchFieldException, IllegalAccessException, URISyntaxException {
        Webhook.sendWebhook(
                ConfigKeys.WEBHOOK_AD_CREATED_EMBED_URL.getString(),
                ConfigKeys.WEBHOOK_AD_CREATED_EMBED_ENABLED.getBoolean(),
                replacePlaceholdersAdCreate(ConfigKeys.WEBHOOK_AD_CREATED_EMBED_DESCRIPTION.getString(), event),
                ConfigKeys.WEBHOOK_AD_CREATED_EMBED_COLOR.getString(),
                replacePlaceholdersAdCreate(ConfigKeys.WEBHOOK_AD_CREATED_EMBED_AUTHOR_NAME.getString(), event),
                replacePlaceholdersAdCreate(ConfigKeys.WEBHOOK_AD_CREATED_EMBED_AUTHOR_URL.getString(), event),
                replacePlaceholdersAdCreate(ConfigKeys.WEBHOOK_AD_CREATED_EMBED_AUTHOR_ICON.getString(), event),
                replacePlaceholdersAdCreate(ConfigKeys.WEBHOOK_AD_CREATED_EMBED_FOOTER_TEXT.getString(), event),
                replacePlaceholdersAdCreate(ConfigKeys.WEBHOOK_AD_CREATED_EMBED_FOOTER_ICON.getString(), event),
                replacePlaceholdersAdCreate(ConfigKeys.WEBHOOK_AD_CREATED_EMBED_THUMBNAIL.getString(), event),
                replacePlaceholdersAdCreate(ConfigKeys.WEBHOOK_AD_CREATED_EMBED_TITLE.getString(), event),
                replacePlaceholdersAdCreate(ConfigKeys.WEBHOOK_AD_CREATED_EMBED_IMAGE.getString(), event)
        );
    }
}
