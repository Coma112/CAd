package net.coma112.cad.manager;

import org.jetbrains.annotations.NotNull;

public record Advertisement(int id, @NotNull String player, @NotNull String title, @NotNull String description, @NotNull String startingDate, @NotNull String endingDate) {}
