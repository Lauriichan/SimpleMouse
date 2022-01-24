package me.lauriichan.school.mouse.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import me.lauriichan.school.mouse.util.source.DataSource;
import me.lauriichan.school.mouse.util.source.PathSource;

public final class ImageCache {

    public static final ImageCache INSTANCE = new ImageCache();

    public static BufferedImage get(String name) {
        return INSTANCE.getOrLoad(name, null);
    }

    public static BufferedImage resource(String name, String path) {
        return INSTANCE.getOrLoad(name, PathSource.ofResource(path));
    }

    private final HashMap<String, BufferedImage> images = new HashMap<>();

    private ImageCache() {}

    public BufferedImage getOrLoad(String name, DataSource source) {
        if (images.containsKey(name)) {
            return images.get(name);
        }
        if (source == null) {
            return null;
        }
        try {
            BufferedImage image = ImageIO.read(source.openStream());
            images.put(name, image);
            return image;
        } catch (IOException e) {
            return null;
        }
    }

    public void clear() {
        images.clear();
    }

}
