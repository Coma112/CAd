package net.coma112.cad.menu;

import lombok.Getter;
import net.coma112.cad.enums.keys.ConfigKeys;
import net.coma112.cad.enums.keys.ItemKeys;
import net.coma112.cad.utils.MenuUtils;
import org.jetbrains.annotations.NotNull;

public abstract class PaginatedMenu extends Menu {

    protected int page = 0;

    public PaginatedMenu(@NotNull MenuUtils menuUtils) {
        super(menuUtils);
    }

    public abstract void addMenuBorder();
    public abstract int getMaxItemsPerPage();
}

