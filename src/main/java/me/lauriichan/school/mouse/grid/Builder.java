package me.lauriichan.school.mouse.grid;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import me.lauriichan.school.mouse.api.IBlock;
import me.lauriichan.school.mouse.api.IBuilder;
import me.lauriichan.school.mouse.api.ICheese;
import me.lauriichan.school.mouse.api.IGrid;
import me.lauriichan.school.mouse.api.IMouse;
import me.lauriichan.school.mouse.grid.object.Cheese;
import me.lauriichan.school.mouse.grid.object.Mouse;
import me.lauriichan.school.mouse.grid.object.Trash;
import me.lauriichan.school.mouse.util.ColorCache;
import me.lauriichan.school.mouse.util.source.PathSource;

public final class Builder implements IBuilder {

    private final Grid grid;

    public Builder(Grid grid) {
        this.grid = grid;
    }

    public IGrid getGrid() {
        return grid;
    }

    public IMouse mouseAt(int x, int y) {
        return mouseAt(x, y, 0);
    }

    public IMouse mouseAt(int x, int y, int speed) {
        Mouse mouse = new Mouse(speed);
        if (!grid.add(mouse)) {
            throw new IllegalStateException("Failed to add Mouse!");
        }
        mouse.setPosition(x, y);
        return mouse;
    }

    public ICheese cheeseAt(int x, int y) {
        return cheeseAt(x, y, 1);
    }

    public ICheese cheeseAt(int x, int y, int amount) {
        Cheese cheese = new Cheese(amount);
        if (!grid.add(cheese)) {
            throw new IllegalStateException("Failed to add Cheese!");
        }
        cheese.setPosition(x, y);
        return cheese;
    }

    public IBlock trashAt(int x, int y) {
        Trash trash = new Trash();
        if (!grid.add(trash)) {
            throw new IllegalStateException("Failed to add Trash!");
        }
        trash.setPosition(x, y);
        return trash;
    }

    @Override
    public void loadImage(File file) throws IllegalStateException {
        try {
            loadImage(ImageIO.read(new PathSource(file.toPath()).openStream()));
        } catch (Throwable throwable) {
            throw new IllegalStateException("Failed to load Grid from image!", throwable);
        }
    }
    
    @Override
    public void loadImage(BufferedImage image) throws IllegalStateException {
        if(image.getWidth() != grid.getWidth() || image.getHeight() != grid.getHeight()) {
            throw new IllegalStateException("Image doesn't match Grid size!");
        }
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                GridObject object = load(new Color(image.getRGB(x, y)));
                if (object == null) {
                    continue;
                }
                if (!grid.add(object)) {
                    throw new IllegalStateException("Failed to add Object!");
                }
                object.setPosition(x, y);
            }
        }
    }

    private GridObject load(Color color) {
        if (compare(color, ColorCache.color("FEFF00"))) {
            return new Cheese(256 - color.getAlpha());
        } else if (compare(color, ColorCache.color("644700"))) {
            return new Trash();
        } else if (compare(color, ColorCache.color("323232"))) {
            return new Mouse((256 - color.getAlpha()) / 11);
        }
        return null;
    }

    private boolean compare(Color c1, Color c2) {
        return c1.getRed() == c2.getRed() && c1.getGreen() == c2.getGreen() && c1.getBlue() == c2.getBlue();
    }

}
