package net.coma112.cad.command;

import net.coma112.cad.CAd;
import net.coma112.cad.enums.keys.MessageKeys;
import net.coma112.cad.menu.menus.CombinedMenu;
import net.coma112.cad.menu.menus.MainMenu;
import net.coma112.cad.utils.MenuUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Command({"cad", "ad"})
public class CommandAdvertisement {
    @Subcommand("menu")
    public void menu(@NotNull Player player) {
        new MainMenu(MenuUtils.getMenuUtils(player)).open();
    }

    @Subcommand("help")
    @DefaultFor({"cad", "ad"})
    public void help(@NotNull CommandSender sender) {
        List<String> helpMessages = MessageKeys.HELP.getMessages();
        helpMessages.forEach(sender::sendMessage);
    }

    @Subcommand("new")
    public void newAdvertisement(@NotNull Player player, @NotNull String text) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String startingDate = dateFormat.format(now);
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        Date endingDate = calendar.getTime();
        String formattedEndingDate = dateFormat.format(endingDate);

        String[] parts = text.split(";", 2);
        String title = parts[0].trim();
        String description = parts.length > 1 ? parts[1].trim() : "";

        CAd.getDatabase().createAdvertisement(player, (title + " ").trim(), (description + " ").trim(), startingDate, formattedEndingDate);
    }
}
