package me.lauriichan.school.mouse.api;

public interface IObject {
    
    int getX();
    
    int getY();
    
    IGrid getGrid();
    
    default boolean isBlocking() {
        return false;
    }
    
    default boolean hasType(Class<?> type) {
        return type.isAssignableFrom(getClass());
    }

}
