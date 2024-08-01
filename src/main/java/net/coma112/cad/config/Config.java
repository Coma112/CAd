package net.coma112.cad.config;


import net.coma112.cad.CAd;
import net.coma112.cad.utils.ConfigUtils;

public class Config extends ConfigUtils {
    public Config() {
        super(CAd.getInstance().getDataFolder().getPath(), "config");
        save();
    }
}
