package net.coma112.cad.menu.menus;

import net.coma112.cad.enums.keys.ConfigKeys;
import net.coma112.cad.enums.keys.ItemKeys;
import net.coma112.cad.menu.Menu;
import net.coma112.cad.utils.MenuUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

public class MainMenu extends Menu implements Listener {

    public MainMenu(@NotNull MenuUtils menuUtils) {
        super(menuUtils);
    }

    @Override
    public String getMenuName() {
        return ConfigKeys.MAIN_TITLE.getString();
    }

    @Override
    public int getMenuTick() {
        return 2000;
    }

    @Override
    public int getSlots() {
        return ConfigKeys.MAIN_SIZE.getInt();
    }

    @Override
    public void handleMenu(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getInventory().equals(inventory)) return;

        event.setCancelled(true);

        if (event.getSlot() == ConfigKeys.MAIN_COMBINED_SLOT.getInt()) {
            inventory.close();
            new CombinedMenu(MenuUtils.getMenuUtils(player)).open();
        }

        if (event.getSlot() == ConfigKeys.MAIN_OWN_SLOT.getInt()) {
            inventory.close();
            new OwnMenu(MenuUtils.getMenuUtils(player)).open();
        }
    }

    @Override
    public void setMenuItems() {
        inventory.clear();
        inventory.setItem(ConfigKeys.MAIN_COMBINED_SLOT.getInt(), ItemKeys.MAIN_COMBINED_MENU.getItem());
        inventory.setItem(ConfigKeys.MAIN_OWN_SLOT.getInt(), ItemKeys.MAIN_OWN_MENU.getItem());
    }

    @EventHandler
    public void onClose(final InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory)) close();
    }
}
