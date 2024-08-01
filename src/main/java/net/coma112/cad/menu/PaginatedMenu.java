package net.coma112.cad.menu;

import net.coma112.cad.manager.Advertisement;
import net.coma112.cad.utils.MenuUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class PaginatedMenu extends Menu {

    protected int page = 0;
    protected int nextPage = page + 1;
    protected int totalPages = (int) Math.ceil((double) getList().size() / getMaxItemsPerPage());
    protected int startIndex = page * getMaxItemsPerPage();
    protected int endIndex = Math.min(startIndex + getMaxItemsPerPage(), getList().size());

    public PaginatedMenu(@NotNull MenuUtils menuUtils) {
        super(menuUtils);
    }

    public abstract void addMenuBorder();
    public abstract int getMaxItemsPerPage();
    public abstract List<Advertisement> getList();
}

