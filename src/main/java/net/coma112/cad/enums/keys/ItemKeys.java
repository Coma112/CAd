package net.coma112.cad.enums.keys;

import net.coma112.cad.item.IItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public enum ItemKeys {
    AD_COMBINED("ad-item-combined"),
    AD_OWN("ad-item-own"),

    MAIN_COMBINED_MENU("menus.main.combined-menu-item"),
    MAIN_OWN_MENU("menus.main.own-menu-item"),

    COMBINED_BACK("menus.combined.back-item"),
    COMBINED_FORWARD("menus.combined.forward-item"),

    OWN_BACK("menus.own.back-item"),
    OWN_FORWARD("menus.own.forward-item");

    private final String path;

    ItemKeys(@NotNull final String path) {
        this.path = path;
    }

    public ItemStack getItem() {
        return IItemBuilder.createItemFromString(path);
    };
}
