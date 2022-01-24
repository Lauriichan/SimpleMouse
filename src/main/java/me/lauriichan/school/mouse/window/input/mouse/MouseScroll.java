package me.lauriichan.school.mouse.window.input.mouse;

import me.lauriichan.school.mouse.util.Point;
import me.lauriichan.school.mouse.window.input.Input;
import me.lauriichan.school.mouse.window.input.InputProvider;

public final class MouseScroll extends Input {

    protected final Point position;
    protected final Point screenPosition;

    protected final int scroll;
    protected final double rotation;

    public MouseScroll(InputProvider provider, int x, int y, int screenX, int screenY, int scroll, double rotation) {
        super(provider);
        this.position = new Point(x, y);
        this.screenPosition = new Point(screenX, screenY);
        this.scroll = scroll;
        this.rotation = rotation;
    }

    public int getX() {
        return position.getX();
    }

    public int getScreenX() {
        return screenPosition.getX();
    }

    public int getY() {
        return position.getY();
    }

    public int getScreenY() {
        return screenPosition.getY();
    }

    public int getScroll() {
        return scroll;
    }

    public double getRotation() {
        return rotation;
    }

}
