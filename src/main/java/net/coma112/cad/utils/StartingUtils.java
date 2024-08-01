package net.coma112.cad.utils;

import net.coma112.cad.CAd;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class StartingUtils {
    public static void registerListenersAndCommands() {
        RegisterUtils.registerEvents();
        RegisterUtils.registerCommands();
    }

    public static void saveResourceIfNotExists(@NotNull String resourcePath) {
        if (!new File(CAd.getInstance().getDataFolder(), resourcePath).exists())
            CAd.getInstance().saveResource(resourcePath, false);
    }
}
