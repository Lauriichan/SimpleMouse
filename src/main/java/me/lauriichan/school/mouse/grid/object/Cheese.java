package me.lauriichan.school.mouse.grid.object;

import java.awt.image.BufferedImage;

import me.lauriichan.school.mouse.api.ICheese;
import me.lauriichan.school.mouse.grid.GridObject;
import me.lauriichan.school.mouse.grid.GridSprite;
import me.lauriichan.school.mouse.util.ImageCache;
import me.lauriichan.school.mouse.window.ui.DragPane;

public class Cheese extends GridObject implements ICheese {

    private static final BufferedImage CHEESE_IMAGE = ImageCache.resource("cheese", "images/cheese.png");

    private final GridSprite sprite = new GridSprite(this, CHEESE_IMAGE);

    private int amount;

    public Cheese(int amount) {
        this.amount = Math.max(Math.abs(amount), 1);
    }

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

    public boolean isAvailable() {
        return amount > 0;
    }

    public void setAmount(int amount) {
        if(!isSet()) {
            return;
        }
        if(amount == 0) {
            getGrid().remove(this);
            return;
        }
        this.amount = Math.max(amount, 1);
    }

    @Override
    public int getAmount() {
        return amount;
    }

}
