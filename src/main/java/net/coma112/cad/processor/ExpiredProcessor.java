package net.coma112.cad.processor;

import net.coma112.cad.CAd;
import net.coma112.cad.utils.AdLogger;

public class ExpiredProcessor {
    public static void startExpirationCheckTask() {
        CAd.getInstance().getScheduler().runTaskTimerAsynchronously(() -> {
            try {
                CAd.getDatabase().deleteExpiredAdvertisements();
            } catch (Exception exception) {
                AdLogger.error(exception.getMessage());
            }
        }, 0L, 20L * 60 * 60);
    }
}
