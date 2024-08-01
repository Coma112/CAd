package net.coma112.cad.menu.menus;

import net.coma112.cad.CAd;
import net.coma112.cad.database.AbstractDatabase;
import net.coma112.cad.enums.keys.ConfigKeys;
import net.coma112.cad.enums.keys.ItemKeys;
import net.coma112.cad.enums.keys.MessageKeys;
import net.coma112.cad.manager.Advertisement;
import net.coma112.cad.menu.PaginatedMenu;
import net.coma112.cad.utils.MenuUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SuppressWarnings("deprecation")
public class CombinedMenu extends PaginatedMenu implements Listener {

    public CombinedMenu(@NotNull MenuUtils menuUtils) {
        super(menuUtils);
    }

    @Override
    public void addMenuBorder() {
        inventory.setItem(ConfigKeys.COMBINED_BACK_SLOT.getInt(), ItemKeys.COMBINED_BACK.getItem());
        inventory.setItem(ConfigKeys.COMBINED_FORWARD_SLOT.getInt(), ItemKeys.COMBINED_FORWARD.getItem());
    }

    @Override
    public String getMenuName() {
        return ConfigKeys.COMBINED_TITLE.getString();
    }

    @Override
    public int getSlots() {
        return ConfigKeys.COMBINED_SIZE.getInt();
    }

    @Override
    public int getMaxItemsPerPage() {
        return ConfigKeys.COMBINED_SIZE.getInt() - 2;
    }

    @Override
    public int getMenuTick() {
        return ConfigKeys.COMBINED_UPDATE_TICK.getInt();
    }

    @Override
    public void setMenuItems() {
        List<Advertisement> advertisements = CAd.getDatabase().getAdvertisements();
        inventory.clear();

        addMenuBorder();

        int startIndex = page * getMaxItemsPerPage();
        int endIndex = Math.min(startIndex + getMaxItemsPerPage(), advertisements.size());

        IntStream.range(startIndex, endIndex).forEach(index -> inventory.addItem(createAdItem(advertisements.get(index))));
    }

    @Override
    public void handleMenu(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getInventory().equals(inventory)) return;

        event.setCancelled(true);

        List<Advertisement> advertisements = CAd.getDatabase().getAdvertisements();
        int clickedIndex = event.getSlot();

        if (clickedIndex == ConfigKeys.COMBINED_FORWARD_SLOT.getInt()) {
            int nextPageIndex = page + 1;
            int totalPages = (int) Math.ceil((double) advertisements.size() / getMaxItemsPerPage());

            if (nextPageIndex >= totalPages) {
                player.sendMessage(MessageKeys.LAST_PAGE.getMessage());
                return;
            } else {
                page++;
                super.open();
            }
        }

        if (clickedIndex == ConfigKeys.COMBINED_BACK_SLOT.getInt()) {
            if (page == 0) {
                player.sendMessage(MessageKeys.FIRST_PAGE.getMessage());
            } else {
                page--;
                super.open();
            }
        }

        if (clickedIndex >= 0 && clickedIndex < advertisements.size()) {
            Advertisement selectedAdvertisement = advertisements.get(clickedIndex);
            Player target = player.getServer().getPlayerExact(selectedAdvertisement.player());

            if (target == null) {
                player.sendMessage("offline a tag");
                return;
            }

            if (target.isDead()) {
                player.sendMessage("halott a tag");
                return;
            }

            inventory.close();
            player.performCommand(ConfigKeys.COMMAND_ON_CLICK
                    .getString()
                    .replace("{target}", target.getName())
                    .replace("{adName}", selectedAdvertisement.title()));
        }
    }

    @EventHandler
    public void onClose(final InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory)) close();
    }

    private static ItemStack createAdItem(@NotNull Advertisement advertisement) {
        ItemStack itemStack = ItemKeys.AD_COMBINED.getItem();
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            String displayName = meta.getDisplayName()
                    .replace("{id}", String.valueOf(advertisement.id()))
                    .replace("{title}", advertisement.title());

            meta.setDisplayName(displayName);

            List<String> lore = meta.getLore();
            if (lore != null) {
                List<String> updatedLore = new ArrayList<>();

                lore.forEach(line -> {
                    String updatedLine = line
                            .replace("{description}", advertisement.description())
                            .replace("{player}", advertisement.player())
                            .replace("{expiration}", advertisement.endingDate());
                    updatedLore.add(updatedLine);
                });

                meta.setLore(updatedLore);
            }
        }

        itemStack.setItemMeta(meta);
        return itemStack;
    }
}