package net.coma112.cad.utils;

import net.coma112.cad.CAd;
import net.coma112.cad.command.CommandAdvertisement;
import net.coma112.cad.listener.AdCreateListener;
import net.coma112.cad.listener.AdRemoveListener;
import net.coma112.cad.menu.MenuListener;
import org.bukkit.event.Listener;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public final class RegisterUtils {
    public static void registerEvents() {
        getListenerClasses().forEach(clazz -> {
            try {
                CAd.getInstance().getServer().getPluginManager().registerEvents(clazz.getDeclaredConstructor().newInstance(), CAd.getInstance());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    public static void registerCommands() {
        BukkitCommandHandler handler = BukkitCommandHandler.create(CAd.getInstance());

        handler.register(new CommandAdvertisement());
    }

    private static Set<Class<? extends Listener>> getListenerClasses() {
        Set<Class<? extends Listener>> listenerClasses = new HashSet<>();

        listenerClasses.add(MenuListener.class);
        listenerClasses.add(AdCreateListener.class);
        listenerClasses.add(AdRemoveListener.class);

        return listenerClasses;
    }
}
