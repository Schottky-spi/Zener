package com.github.schottky.zener.menu;

public interface ScrollableMenu extends Menu {

    boolean scrollDown(int rows);

    boolean scrollUp(int rows);

    default boolean scrollOneUp() {
        return scrollUp(1);
    }
    
    default boolean scrollOneDown() {
        return scrollDown(1);
    }
}
