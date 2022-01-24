package me.lauriichan.school.mouse.window.ui.component;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.Objects;

import me.lauriichan.school.mouse.util.Area;
import me.lauriichan.school.mouse.util.Rotation;
import me.lauriichan.school.mouse.window.ui.Component;

public class Sprite extends Component {

    private final Image image;
    private final Rotation original;

    private final int imgWidth;
    private final int imgHeight;

    private final AffineTransform transform = new AffineTransform();

    private Rotation rotation;

    public Sprite(Image image) {
        this(image, Rotation.NORTH);
    }

    public Sprite(Image image, Rotation rotation) {
        this.image = Objects.requireNonNull(image);
        this.original = Objects.requireNonNull(rotation);
        setRotation(original);
        this.imgWidth = image.getWidth(null);
        this.imgHeight = image.getHeight(null);
        setWidth(imgWidth);
        setHeight(imgHeight);
    }

    public void setRotation(Rotation rotation) {
        if (rotation == this.rotation) {
            return;
        }
        if (this.rotation != original && this.rotation != null) {
            transform.quadrantRotate(original.ordinal() - this.rotation.ordinal(), imgWidth / 2, imgHeight / 2);
        }
        this.rotation = Objects.requireNonNull(rotation);
        transform.quadrantRotate(rotation.ordinal() - original.ordinal(), imgWidth / 2, imgHeight / 2);
    }

    public Rotation getRotation() {
        return rotation;
    }

    @Override
    public void render(Area area) {
        Graphics2D graphics = area.getGraphics();
        AffineTransform old = graphics.getTransform();
        graphics.setTransform(transform);
        graphics.drawImage(image, 0, 0, imgWidth, imgHeight, null);
        graphics.setTransform(old);
    }

    @Override
    public boolean isUpdating() {
        return false;
    }

}
