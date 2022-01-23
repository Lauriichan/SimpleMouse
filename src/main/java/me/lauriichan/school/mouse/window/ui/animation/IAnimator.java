package me.lauriichan.school.mouse.window.ui.animation;

@FunctionalInterface
public interface IAnimator<E> {
    
    E update(E start, E end, double percentage);
    
}
