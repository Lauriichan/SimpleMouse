package me.lauriichan.school.mouse.util.tick;

@FunctionalInterface
public interface ITickReceiver {
    
    void onTick(long deltaTime);

}
