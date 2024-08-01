package net.coma112.cad.database;

import net.coma112.cad.manager.Advertisement;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractDatabase {
    public abstract void createAdvertisement(@NotNull Player player, @NotNull String title, @NotNull String description, @NotNull String startingDate, @NotNull String endingDate);

    public abstract void deleteAdvertisement(@NotNull Advertisement advertisement);

    public abstract void deleteExpiredAdvertisements();

    public abstract List<Advertisement> getAdvertisements();

    public abstract List<Advertisement> getAdvertisementsFromPlayer(@NotNull Player playerToGet);

    public abstract boolean isConnected();

    public abstract void disconnect();

    public abstract void createTable();
}
