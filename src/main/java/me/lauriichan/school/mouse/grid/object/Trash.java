package me.lauriichan.school.mouse.grid.object;

import java.awt.image.BufferedImage;

import me.lauriichan.school.mouse.api.IBlock;
import me.lauriichan.school.mouse.grid.GridObject;
import me.lauriichan.school.mouse.grid.GridSprite;
import me.lauriichan.school.mouse.util.ImageCache;
import me.lauriichan.school.mouse.window.ui.DragPane;

public class Trash extends GridObject implements IBlock {

    private static final BufferedImage WALL_IMAGE = ImageCache.resource("trash", "images/WoodOrSmth.png");

    private final GridSprite sprite = new GridSprite(this, WALL_IMAGE);

    public GridSprite getSprite() {
        return sprite;
    }

    @Override
    protected void onRegisterComponents(int idx, DragPane pane) {
        pane.addChildAt(idx, sprite);
    }

    @Override
    protected void onUnregisterComponents(DragPane pane) {
        pane.removeChild(sprite);
    }
    
    @Override
    protected void onMoveSuccess(int x, int y) {
        sprite.setX(asGridPosition(x));
        sprite.setY(asGridPosition(y));
    }

}
