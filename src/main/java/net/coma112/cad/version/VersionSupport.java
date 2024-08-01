package net.coma112.cad.version;

import lombok.Getter;
import net.coma112.cad.utils.AdLogger;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

@Getter
public class VersionSupport {
    private final ServerVersionSupport versionSupport;

    public VersionSupport(@NotNull Plugin plugin, @NotNull MinecraftVersion version) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (version == MinecraftVersion.UNKNOWN) throw new IllegalArgumentException("VERSION NOT FOUND!!! ");


        Class<?> clazz = Class.forName("net.coma112.cad.version.nms." + version.name() + ".Version");
        versionSupport = (ServerVersionSupport) clazz.getConstructor(Plugin.class).newInstance(plugin);

        if (!versionSupport.isSupported()) {
            AdLogger.warn("---   VERSION IS SUPPORTED BUT,   ---");
            AdLogger.warn("The version you are using is badly");
            AdLogger.warn("implemented. Many features won't work.");
            AdLogger.warn("Please consider updating your server ");
            AdLogger.warn("version to a newer version. (like 1.19_R2)");
            AdLogger.warn("---   PLEASE READ THIS MESSAGE!   ---");
        }
    }
}
