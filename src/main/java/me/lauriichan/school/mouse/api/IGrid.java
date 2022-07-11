package me.lauriichan.school.mouse.api;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import me.lauriichan.school.mouse.grid.SimpleGrid;
import me.lauriichan.school.mouse.util.source.PathSource;

public interface IGrid {

    IBuilder getBuilder();

    int getWidth();

    int getHeight();

    boolean isEmpty();

    boolean isInBounds(int x, int y);

    <E extends IObject> boolean hasObject(Class<E> type);

    int getObjectCount();

    <E extends IObject> int getObjectCount(Class<E> type);

    IObject[] getObjects();

    <E extends IObject> E[] getObjects(Class<E> type);

    boolean hasObjectAt(int x, int y);

    <E extends IObject> boolean hasObjectAt(int x, int y, Class<E> type);

    int getCountAt(int x, int y);

    <E extends IObject> int getCountAt(int x, int y, Class<E> type);

    IObject[] getObjectsAt(int x, int y);

    <E extends IObject> E[] getObjectsAt(int x, int y, Class<E> type);

    boolean isHidden();

    void setFps(int fps);

    void setTps(int tps);

    int getFps();

    int getTps();

    void show();

    void hide();

    void center();

    void close();

    void clear();

    void setClosingAction(Runnable action);

    Runnable getClosingAction();

    /*
     * Grid Creator
     */

    public static IGrid newGrid(int cells) {
        return new SimpleGrid(cells, 128);
    }

    public static IGrid newGrid(int cellX, int cellY) {
        return new SimpleGrid(cellX, cellY, 128);
    }

    public static IGrid fromImage(File file) {
        try {
            return fromImage(ImageIO.read(new PathSource(file.toPath()).openStream()));
        } catch (Throwable throwable) {
            throw new IllegalStateException("Failed to load Grid from image!", throwable);
        }
    }

    public static IGrid fromImage(BufferedImage image) {
        SimpleGrid grid = new SimpleGrid(image.getWidth(), image.getHeight(), 128);
        grid.getBuilder().loadImage(image);
        return grid;
    }

}
