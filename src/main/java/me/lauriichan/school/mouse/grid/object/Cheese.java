package me.lauriichan.school.mouse.grid.object;

import java.awt.image.BufferedImage;

import me.lauriichan.school.mouse.grid.GridObject;
import me.lauriichan.school.mouse.grid.GridSprite;
import me.lauriichan.school.mouse.util.ImageCache;
import me.lauriichan.school.mouse.window.ui.Pane;

public class Cheese extends GridObject {

    private static final BufferedImage CHEESE_IMAGE = ImageCache.resource("cheese", "images/cheese.png");

    private final GridSprite sprite = new GridSprite(this, CHEESE_IMAGE);

    public GridSprite getSprite() {
        return sprite;
    }

    @Override
    protected void onRegisterComponents(Pane pane) {
        pane.addChild(sprite);
    }

    @Override
    protected void onUnregisterComponents(Pane pane) {
        pane.removeChild(sprite);
    }

}
