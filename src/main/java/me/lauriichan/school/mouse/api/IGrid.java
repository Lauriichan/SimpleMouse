package me.lauriichan.school.mouse.api;

import me.lauriichan.school.mouse.grid.SimpleGrid;

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
    
    void show();
    
    void hide();
    
    void center();
    
    void close();

    /*
     * Grid Creator
     */

    public static IGrid newGrid(int cells) {
        return new SimpleGrid(cells, 128);
    }

    public static IGrid newGrid(int cellX, int cellY) {
        return new SimpleGrid(cellX, cellY, 128);
    }

}
