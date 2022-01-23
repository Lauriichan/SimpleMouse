package me.lauriichan.school.mouse.window.ui;

import me.lauriichan.school.mouse.window.util.Area;

public interface IComponent {

    default boolean isRoot() {
        return false;
    }

    default int getY() {
        return 0;
    }

    default int getX() {
        return 0;
    }
    
    default int getWidth() {
        return 0;
    }
    
    default int getHeight() {
        return 0;
    }
    
    boolean isUpdating();
    
    void setUpdating(boolean update);

    boolean isHidden();

    void setHidden(boolean hidden);

    void render(Area area);

    void update(long deltaTime);

    default void exit() {}

}
