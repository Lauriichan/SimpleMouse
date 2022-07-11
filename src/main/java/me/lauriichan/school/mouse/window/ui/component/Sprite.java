package me.lauriichan.school.mouse.window.ui.component;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.Objects;

import me.lauriichan.school.mouse.util.Area;
import me.lauriichan.school.mouse.util.Rotation;
import me.lauriichan.school.mouse.window.ui.Component;

public class Sprite extends Component {

    private Image image;
    private Rotation original;

    private int imgWidth;
    private int imgHeight;

    private int imgWidthHalf;
    private int imgHeightHalf;

    private Rotation rotation;
    private int amount = 0;

    public Sprite(Image image) {
        this(image, Rotation.NORTH);
    }

    public Sprite(Image image, Rotation rotation) {
        setOriginal(image, rotation);
    }
    
    public void setOriginal(Image image, Rotation rotation) {
        this.image = Objects.requireNonNull(image);
        this.original = Objects.requireNonNull(rotation);
        setRotation(original);
        this.imgWidth = image.getWidth(null);
        this.imgHeight = image.getHeight(null);
        this.imgWidthHalf = imgWidth / 2;
        this.imgHeightHalf = imgHeight / 2;
        setWidth(imgWidth);
        setHeight(imgHeight);
    }

    public void setRotation(Rotation rotation) {
        if (rotation == this.rotation) {
            return;
        }
        this.rotation = Objects.requireNonNull(rotation);
        this.amount = (rotation.ordinal() - original.ordinal());
    }

    public Rotation getRotation() {
        return rotation;
    }

    @Override
    public void render(Area area) {
        Graphics2D graphics = area.getGraphics();
        AffineTransform old = graphics.getTransform();
        AffineTransform transform = (AffineTransform) old.clone();
        transform.quadrantRotate(amount, imgWidthHalf, imgHeightHalf);
        graphics.setTransform(transform);
        graphics.drawImage(image, 0, 0, imgWidth, imgHeight, null);
        graphics.setTransform(old);
    }

    @Override
    public boolean isUpdating() {
        return false;
    }

}
