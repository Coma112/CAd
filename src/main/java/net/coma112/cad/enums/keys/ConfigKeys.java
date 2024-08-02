package net.coma112.cad.enums.keys;

import net.coma112.cad.CAd;
import net.coma112.cad.processor.MessageProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum ConfigKeys {
    LANGUAGE("language"),
    DATABASE("database.type"),
    DATEFORMAT("date-format"),
    COMMAND_ON_CLICK("command-on-click"),
    EXPIRES_IN("expires-in"),
    TITLE_DESCRIPTION_SEPARATOR("title-description-split-sign"),
    ADVERTISEMENT_BROADCAST("advertisement-broadcast"),
    MAX_AD("max-ads-per-player"),
    AD_PRICE("ad-price"),

    MAIN_TITLE("menus.main.title"),
    MAIN_SIZE("menus.main.size"),
    MAIN_COMBINED_SLOT("menus.main.combined-menu-item.slot"),
    MAIN_OWN_SLOT("menus.main.own-menu-item.slot"),

    COMBINED_TITLE("menus.combined.title"),
    COMBINED_SIZE("menus.combined.size"),
    COMBINED_UPDATE_TICK("menus.combined.update-tick"),
    COMBINED_BACK_SLOT("menus.combined.back-item.slot"),
    COMBINED_FORWARD_SLOT("menus.combined.forward-item.slot"),
    COMBINED_BACK_TO_MAIN_SLOT("menus.combined.back-to-main-item.slot"),

    OWN_TITLE("menus.own.title"),
    OWN_BACK_SLOT("menus.own.back-item.slot"),
    OWN_FORWARD_SLOT("menus.own.forward-item.slot"),
    OWN_SIZE("menus.own.size"),
    OWN_BACK_TO_MAIN_SLOT("menus.own.back-to-main-item.slot"),
    OWN_UPDATE_TICK("menus.own.update-tick");


    private final String path;

    ConfigKeys(@NotNull final String path) {
        this.path = path;
    }

    public String getString() {
        return MessageProcessor.process(CAd.getInstance().getConfiguration().getString(path));
    }

    public boolean getBoolean() {
        return CAd.getInstance().getConfiguration().getBoolean(path);
    }

    public int getInt() {
        return CAd.getInstance().getConfiguration().getInt(path);
    }

    public List<String> getStrings() {
        return CAd.getInstance().getConfiguration().getList(path)
                .stream()
                .map(MessageProcessor::process)
                .toList();
    }
}
