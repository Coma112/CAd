package net.coma112.cad.utils;

import net.coma112.cad.CAd;
import net.coma112.cad.version.MinecraftVersion;
import net.coma112.cad.version.ServerVersionSupport;
import net.coma112.cad.version.VersionSupport;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.coma112.cad.version.MinecraftVersion.determineVersion;

public final class StartingUtils {
    private static boolean isSupported;

    public static void registerListenersAndCommands() {
        RegisterUtils.registerEvents();
        RegisterUtils.registerCommands();
    }

    public static void saveResourceIfNotExists(@NotNull String resourcePath) {
        if (!new File(CAd.getInstance().getDataFolder(), resourcePath).exists())
            CAd.getInstance().saveResource(resourcePath, false);
    }

    public static void startExpirationCheckTask() {
        CAd.getInstance().getScheduler().runTaskTimerAsynchronously(() -> {
            try {
                CAd.getDatabase().deleteExpiredAdvertisements();
            } catch (Exception exception) {
                AdLogger.error(exception.getMessage());
            }
        }, 0L, 20L * 60 * 60);
    }

    public static void checkVM() {
        int vmVersion = getVMVersion();
        if (vmVersion < 11) {
            Bukkit.getPluginManager().disablePlugin(CAd.getInstance());
            return;
        }

        if (!isSupported) {
            AdLogger.error("This version of CAd is not supported on this server version.");
            AdLogger.error("Please consider updating your server version to a newer version.");
            CAd.getInstance().getServer().getPluginManager().disablePlugin(CAd.getInstance());
        }
    }

    public static void checkVersion() {
        try {
            Class.forName("org.spigotmc.SpigotConfig");
        } catch (Exception ignored) {
            isSupported = false;
            return;
        }

        try {
            String bukkitVersion = Bukkit.getVersion();

            Pattern pattern = Pattern.compile("\\(MC: (\\d+)\\.(\\d+)(?:\\.(\\d+))?\\)");
            Matcher matcher = pattern.matcher(bukkitVersion);

            if (matcher.find()) {
                int majorVersion = Integer.parseInt(matcher.group(1));
                int minorVersion = Integer.parseInt(matcher.group(2));
                int patchVersion = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) : 0;

                MinecraftVersion version = determineVersion(majorVersion, minorVersion, patchVersion);
                if (version == MinecraftVersion.UNKNOWN) {
                    isSupported = false;
                    return;
                }

                VersionSupport support = new VersionSupport(CAd.getInstance(), version);
                ServerVersionSupport nms = support.getVersionSupport();
                isSupported = nms != null;

            } else {
                isSupported = false;
            }
        } catch (Exception exception) {
            isSupported = false;
        }
    }

    static int getVMVersion() {
        String javaVersion = System.getProperty("java.version");
        Matcher matcher = Pattern.compile("(?:1\\.)?(\\d+)").matcher(javaVersion);

        if (!matcher.find()) return -1;

        String version = matcher.group(1);

        try {
            return Integer.parseInt(version);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}
