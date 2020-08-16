package com.github.schottky.zener.menu.paged;

import com.github.schottky.zener.menu.item.MenuItem;
import org.jetbrains.annotations.NotNull;

public class ContainerPagedMenu extends AbstractPagedMenu {

    public ContainerPagedMenu(
            int rows,
            @NotNull String title,
            FlowStyle flow,
            MenuItem[] initialElements)
    {
        super(rows, title);
        MenuItem[][] current = newEmptyPage();
        int currentPage = 1;
        int index = 0;
        loop:
        while (true) {
            if (flow.horizontalFirst()) {
                for (lastY = flow.startY(rowCount() - 1); flow.condY(lastY, rowCount() - 1); lastY += flow.modY()) {
                    for (lastX = flow.startX(columnCount()); flow.condX(lastX, columnCount()); lastX += flow.modX()) {
                        if (index >= initialElements.length) {
                            this.setPage(currentPage, current);
                            break loop;
                        }
                        current[lastX][lastY] = initialElements[index];
                        index++;
                    }
                }
            } else {
                for (lastX = flow.startX(columnCount()); flow.condX(lastX, columnCount()); lastX += flow.modX()) {
                    for (lastY = flow.startY(rowCount() - 1); flow.condY(lastY, rowCount() - 1); lastY += flow.modY()) {
                        if (index >= initialElements.length) {
                            this.setPage(currentPage, current);
                            break loop;
                        }
                        current[lastX][lastY] = initialElements[index];
                        index++;
                    }
                }
            }
            this.setPage(currentPage, current);
            currentPage++;
            current = newEmptyPage();
        }
    }

    private int lastX;
    private int lastY;

    public void addItem(MenuItem item) {
        int page = this.pageCount();
        if (lastX == columnCount() - 1 && lastY == rowCount() - 2) {
            lastX = 0;
            lastY = 0;
            page++;
        } else if (lastX == columnCount() - 1) {
            lastX = 0;
            lastY += 1;
        } else {
            lastX += 1;
        }
        this.setItem(page, lastX, lastY, item);
    }
}
