package me.lauriichan.school.mouse.grid;

import java.awt.Image;
import java.util.Objects;

import me.lauriichan.school.mouse.util.Rotation;
import me.lauriichan.school.mouse.window.ui.component.Sprite;

public final class GridSprite extends Sprite {

    private final GridObject delegate;

    public GridSprite(GridObject delegate, Image image) {
        super(image);
        this.delegate = Objects.requireNonNull(delegate);
    }

    public GridSprite(GridObject delegate, Image image, Rotation rotation) {
        super(image, rotation);
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public void update(long deltaTime) {
        delegate.update(deltaTime);
    }

    @Override
    public boolean isUpdating() {
        return delegate.isUpdating() && update;
    }

}
