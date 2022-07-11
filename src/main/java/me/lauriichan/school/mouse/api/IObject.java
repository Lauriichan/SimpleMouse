package me.lauriichan.school.mouse.api;

public interface IObject {
    
    int getX();
    
    int getY();
    
    IGrid getGrid();
    
    default double distance(IObject object) {
        return square(getX() - object.getX()) + square(getY() - object.getY());
    }
    
    private static double square(double in) {
        return in * in;
    }
    
    default boolean isBlocking() {
        return false;
    }
    
    default boolean hasType(Class<?> type) {
        return type.isAssignableFrom(getClass());
    }

}
