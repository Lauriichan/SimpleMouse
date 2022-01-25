package me.lauriichan.school.mouse.window.ui.component.geometry;

import java.awt.Color;

import me.lauriichan.school.mouse.util.Area;
import me.lauriichan.school.mouse.window.input.Listener;
import me.lauriichan.school.mouse.window.input.mouse.MouseButton;
import me.lauriichan.school.mouse.window.input.mouse.MouseDrag;
import me.lauriichan.school.mouse.window.ui.Panel;

public final class DragRectangle extends Geometry {
    
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

    @Listener
    public void onDrag(MouseDrag drag) {
        if (drag.isConsumed() || drag.getOldY() > getHeight() || drag.getButton() != MouseButton.LEFT) {
            return;
        }
        drag.consume();
        Panel panel = drag.getProvider().getPanel();
        panel.setPosition(panel.getX() + drag.getX() - drag.getOldX(), panel.getY() + drag.getY() - drag.getOldY());
    }

}
