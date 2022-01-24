package me.lauriichan.school.mouse.api;

public interface IBlock extends IObject {
    
    default boolean isBlocking() {
        return true;
    }

}
