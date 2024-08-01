package net.coma112.cad.version.nms.v1_20_R6;

import net.coma112.cad.utils.AdLogger;
import net.coma112.cad.version.ServerVersionSupport;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Version implements ServerVersionSupport {

    @Contract(pure = true)
    public Version(@NotNull Plugin plugin) {
        AdLogger.info("Loading support for version 1.20.6...");
        AdLogger.info("Support for 1.20.6 is loaded!");
    }

    @Override
    public String getName() {
        return "1.20_R6";
    }

    @Override
    public boolean isSupported() {
        return true;
    }
}
