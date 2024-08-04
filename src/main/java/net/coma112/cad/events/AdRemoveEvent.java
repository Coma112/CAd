package net.coma112.cad.events;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public final class AdRemoveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final int id;
    private final String player;
    private final String title;
    private final String description;
    private final String startingDate;
    private final String endingDate;

    public AdRemoveEvent(final int id, final @NotNull String player, final @NotNull String title, final @NotNull String description, final @NotNull String endingDate, final @NotNull String startingDate) {
        this.id = id;
        this.player = player;
        this.title = title;
        this.description = description;
        this.endingDate = endingDate;
        this.startingDate = startingDate;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
