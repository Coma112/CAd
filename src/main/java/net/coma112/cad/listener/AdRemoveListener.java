package net.coma112.cad.listener;

import net.coma112.cad.enums.keys.ConfigKeys;
import net.coma112.cad.events.AdRemoveEvent;
import net.coma112.cad.hooks.Webhook;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;
import java.net.URISyntaxException;

import static net.coma112.cad.hooks.Webhook.replacePlaceholdersAdRemove;

public class AdRemoveListener implements Listener {
    @EventHandler
    public void onRemove(final AdRemoveEvent event) throws IOException, NoSuchFieldException, IllegalAccessException, URISyntaxException {
        Webhook.sendWebhook(
                ConfigKeys.WEBHOOK_AD_REMOVE_EMBED_URL.getString(),
                ConfigKeys.WEBHOOK_AD_REMOVE_EMBED_ENABLED.getBoolean(),
                replacePlaceholdersAdRemove(ConfigKeys.WEBHOOK_AD_REMOVE_EMBED_DESCRIPTION.getString(), event),
                ConfigKeys.WEBHOOK_AD_REMOVE_EMBED_COLOR.getString(),
                replacePlaceholdersAdRemove(ConfigKeys.WEBHOOK_AD_REMOVE_EMBED_AUTHOR_NAME.getString(), event),
                replacePlaceholdersAdRemove(ConfigKeys.WEBHOOK_AD_REMOVE_EMBED_AUTHOR_URL.getString(), event),
                replacePlaceholdersAdRemove(ConfigKeys.WEBHOOK_AD_REMOVE_EMBED_AUTHOR_ICON.getString(), event),
                replacePlaceholdersAdRemove(ConfigKeys.WEBHOOK_AD_REMOVE_EMBED_FOOTER_TEXT.getString(), event),
                replacePlaceholdersAdRemove(ConfigKeys.WEBHOOK_AD_REMOVE_EMBED_FOOTER_ICON.getString(), event),
                replacePlaceholdersAdRemove(ConfigKeys.WEBHOOK_AD_REMOVE_EMBED_THUMBNAIL.getString(), event),
                replacePlaceholdersAdRemove(ConfigKeys.WEBHOOK_AD_REMOVE_EMBED_TITLE.getString(), event),
                replacePlaceholdersAdRemove(ConfigKeys.WEBHOOK_AD_REMOVE_EMBED_IMAGE.getString(), event)
        );
    }
}
