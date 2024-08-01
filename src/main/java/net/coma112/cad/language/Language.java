package net.coma112.cad.language;

import net.coma112.cad.CAd;
import net.coma112.cad.utils.ConfigUtils;
import org.jetbrains.annotations.NotNull;
import java.io.File;

public class Language extends ConfigUtils {
    public Language(@NotNull String name) {
        super(CAd.getInstance().getDataFolder().getPath() + File.separator + "locales", name);
        save();
    }
}
