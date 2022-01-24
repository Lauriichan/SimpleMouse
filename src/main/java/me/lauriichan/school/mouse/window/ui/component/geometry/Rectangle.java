package me.lauriichan.school.mouse.window.ui.component.geometry;

import java.awt.Color;

import me.lauriichan.school.mouse.util.Area;

public final class Rectangle extends Geometry {
    
    private Color color = Color.BLACK;
    
    public Color getColor() {
        return color;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    @Override
    public void render(Area area) {
        area.fill(color);
    }

}
