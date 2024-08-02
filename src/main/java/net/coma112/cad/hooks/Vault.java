package net.coma112.cad.hooks;

import lombok.Getter;
import net.coma112.cad.CAd;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class Vault {
    @Getter
    private static Economy economy = null;

    private Vault() {
    }

    private static void setupEconomy() {
        RegisteredServiceProvider<Economy> registeredServiceProvider = CAd.getInstance().getServer().getServicesManager().getRegistration(Economy.class);

        if (registeredServiceProvider != null) economy = registeredServiceProvider.getProvider();
    }

    static {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) setupEconomy();
    }
}
