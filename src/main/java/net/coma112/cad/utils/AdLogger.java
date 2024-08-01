package net.coma112.cad.utils;

import net.coma112.cad.processor.MessageProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class AdLogger {
    static final Logger logger = LogManager.getLogger("CAd");

    public static void info(@NotNull String msg, @NotNull Object... objs) {
        logger.info(msg, objs);
    }

    public static void warn(@NotNull String msg, @NotNull Object... objs) {
        logger.warn(msg, objs);
    }

    public static void error(@NotNull String msg, @NotNull Object... objs) {
        logger.error(msg, objs);
    }

    public static void colored(@NotNull String msg, @NotNull Object... objs) {
        final Message message = new ParameterizedMessage(msg, objs);
        Bukkit.getConsoleSender().sendMessage(MessageProcessor.process(message.getFormattedMessage()));
    }

}
