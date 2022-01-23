package me.lauriichan.school.mouse.window.util;

import java.awt.Color;

@FunctionalInterface
public interface IBoxRenderer {
    
    void render(Area area, Color color, int offset, int size, int thickness);
    
}