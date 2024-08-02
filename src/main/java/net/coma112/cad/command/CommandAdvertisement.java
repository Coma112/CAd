package net.coma112.cad.command;

import net.coma112.cad.CAd;
import net.coma112.cad.enums.keys.ConfigKeys;
import net.coma112.cad.enums.keys.MessageKeys;
import net.coma112.cad.hooks.Vault;
import net.coma112.cad.menu.menus.MainMenu;
import net.coma112.cad.utils.MenuUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Command({"cad", "ad"})
public class CommandAdvertisement {
    @Subcommand("menu")
    @CommandPermission("cad.menu")
    public void menu(@NotNull Player player) {
        new MainMenu(MenuUtils.getMenuUtils(player)).open();
    }

    @Subcommand("help")
    @DefaultFor({"cad", "ad"})
    public void help(@NotNull CommandSender sender) {
        MessageKeys.HELP.getMessages().forEach(sender::sendMessage);
    }

    @Subcommand("reload")
    @CommandPermission("cad.reload")
    public void reload(@NotNull CommandSender sender) {
        CAd.getInstance().getLanguage().reload();
        CAd.getInstance().getConfiguration().reload();
        sender.sendMessage(MessageKeys.RELOAD.getMessage());
    }

    @Subcommand("new")
    @CommandPermission("cad.new")
    public void newAdvertisement(@NotNull Player player, @NotNull String text) {
        if (CAd.getDatabase().getAdvertisementsCount(player) >= ConfigKeys.MAX_AD.getInt()) {
            player.sendMessage(MessageKeys.REACHED_MAX_ADS.getMessage());
            return;
        }

        if (Vault.getEconomy().getBalance(player) < ConfigKeys.AD_PRICE.getInt()) {
            player.sendMessage(MessageKeys.NOT_ENOUGH_MONEY.getMessage());
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(ConfigKeys.DATEFORMAT.getString());
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, ConfigKeys.EXPIRES_IN.getInt());

        if (!text.contains(ConfigKeys.TITLE_DESCRIPTION_SEPARATOR.getString())) {
            player.sendMessage(MessageKeys.NEW_USAGE.getMessage());
            return;
        }

        String[] parts = text.split(ConfigKeys.TITLE_DESCRIPTION_SEPARATOR.getString(), 2);
        String title = parts[0].trim();
        String description = parts[1].trim();

        CAd.getDatabase().createAdvertisement(player, (title + " ").trim(), (description + " ").trim(), dateFormat.format(now), dateFormat.format(calendar.getTime()));
        Vault.getEconomy().withdrawPlayer(player, ConfigKeys.AD_PRICE.getInt());
        Bukkit.getOnlinePlayers().forEach(players -> ConfigKeys.ADVERTISEMENT_BROADCAST.getStrings().forEach(line -> {
            String message = line
                    .replace("{title}", title)
                    .replace("{description}", description)
                    .replace("{player}", player.getName());
            players.sendMessage(message);
        }));
    }

    @Subcommand("remove")
    @CommandPermission("cad.remove")
    public void removeAdvertisement(@NotNull Player player, int id) {
        if (!CAd.getDatabase().exists(id)) {
            player.sendMessage(MessageKeys.NOT_EXISTS.getMessage());
            return;
        }

        CAd.getDatabase().deleteAdvertisement(id);
        player.sendMessage(MessageKeys.SUCCESSFUL_REMOVE.getMessage());
    }
}
