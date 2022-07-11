package me.lauriichan.school.mouse.grid;

import java.awt.Color;
import java.lang.reflect.Array;
import java.util.ArrayList;

import me.lauriichan.school.mouse.api.IBuilder;
import me.lauriichan.school.mouse.api.IObject;
import me.lauriichan.school.mouse.util.DynamicArray;
import me.lauriichan.school.mouse.window.ui.component.geometry.Rectangle;

public class SimpleGrid extends Grid {

    public SimpleGrid(int cellAmount, int cellSize) {
        super(cellAmount, cellSize);
    }

    public SimpleGrid(int cellAmountX, int cellAmountY, int cellSize) {
        super(cellAmountX, cellAmountY, cellSize);
    }

    private final DynamicArray<GridObject> objects = new DynamicArray<>();
    private final Builder builder = new Builder(this);
    private GridObject[] array = new GridObject[0];
    private int backgroundIdx;

    @Override
    public IBuilder getBuilder() {
        return builder;
    }

    @Override
    protected void onSetup() {
        pane.setContentSizeX(amount.getX() * size);
        pane.setContentSizeY(amount.getY() * size);
        panel.setWidth(800);
        panel.setHeight(640);
        panel.setBarHeight(40);
    }

    @Override
    protected void onGridSetup() {
        int col = 0;
        int idx = 0;
        for (int k = 0; k < amount.getY(); k++) {
            for (int i = 0; i < amount.getX(); i++) {
                Rectangle rect = new Rectangle();
                rect.setColor(col++ % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                rect.setWidth(size);
                rect.setHeight(size);
                rect.setX(size * i);
                rect.setY(size * k);
                pane.addChild(rect);
                idx++;
            }
            col++;
        }
        backgroundIdx = idx;
    }

    public int getBackgroundIdx() {
        return backgroundIdx;
    }

    @Override
    public boolean isEmpty() {
        return array.length == 0;
    }

    @Override
    public <E extends IObject> boolean hasObject(Class<E> type) {
        GridObject[] objects = array;
        for (int i = 0; i < objects.length; i++) {
            GridObject object = objects[i];
            if (object.hasType(type)) {
                return true;
            }
        }
        return false;

    }

    @Override
    public int getObjectCount() {
        return array.length;
    }

    @Override
    public <E extends IObject> int getObjectCount(Class<E> type) {
        GridObject[] objects = array;
        int count = 0;
        for (int i = 0; i < objects.length; i++) {
            GridObject object = objects[i];
            if (object.hasType(type)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public IObject[] getObjects() {
        GridObject[] objects = array;
        IObject[] output = new IObject[objects.length];
        System.arraycopy(objects, 0, output, 0, objects.length);
        return output;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends IObject> E[] getObjects(Class<E> type) {
        if (type == null) {
            return (E[]) Array.newInstance(type, 0);
        }
        GridObject[] objects = array;
        ArrayList<E> list = new ArrayList<>();
        for (int i = 0; i < objects.length; i++) {
            GridObject object = objects[i];
            if (object.hasType(type)) {
                list.add(type.cast(object));
            }
        }
        return list.toArray((E[]) Array.newInstance(type, size));
    }

    @Override
    public boolean hasObjectAt(int x, int y) {
        GridObject[] objects = array;
        for (int i = 0; i < objects.length; i++) {
            GridObject object = objects[i];
            if (object.isAt(x, y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <E extends IObject> boolean hasObjectAt(int x, int y, Class<E> type) {
        GridObject[] objects = array;
        for (int i = 0; i < objects.length; i++) {
            GridObject object = objects[i];
            if (object.isAt(x, y) && object.hasType(type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getCountAt(int x, int y) {
        GridObject[] objects = array;
        int count = 0;
        for (int i = 0; i < objects.length; i++) {
            GridObject object = objects[i];
            if (object.isAt(x, y)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public <E extends IObject> int getCountAt(int x, int y, Class<E> type) {
        if (type == null) {
            return 0;
        }
        GridObject[] objects = array;
        int count = 0;
        for (int i = 0; i < objects.length; i++) {
            GridObject object = objects[i];
            if (object.isAt(x, y) && object.hasType(type)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public IObject[] getObjectsAt(int x, int y) {
        ArrayList<IObject> list = new ArrayList<>();
        GridObject[] objects = array;
        for (int i = 0; i < objects.length; i++) {
            GridObject object = objects[i];
            if (object.isAt(x, y)) {
                list.add(object);
            }
        }
        return list.toArray(IObject[]::new);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends IObject> E[] getObjectsAt(int x, int y, Class<E> type) {
        if (type == null) {
            return (E[]) Array.newInstance(type, 0);
        }
        ArrayList<E> list = new ArrayList<>();
        GridObject[] objects = array;
        for (int i = 0; i < objects.length; i++) {
            GridObject object = objects[i];
            if (object.isAt(x, y) && object.hasType(type)) {
                list.add(type.cast(object));
            }
        }
        return list.toArray((E[]) Array.newInstance(type, list.size()));
    }

    @Override
    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < amount.getX() && y >= 0 && y < amount.getY();
    }

    @Override
    public boolean add(GridObject object) {
        if (object == null || objects.indexOf(object) != -1) {
            return false;
        }
        object.setGrid(this);
        objects.add(object);
        array = objects.asArray(GridObject[]::new);
        return true;
    }

    @Override
    public boolean remove(GridObject object) {
        boolean tmp = objects.remove(object);
        if (tmp) {
            object.setGrid(null);
            array = objects.asArray(GridObject[]::new);
        }
        return tmp;
    }

    @Override
    public void clear() {
        while (objects.length() != 0) {
            remove(objects.get(0));
        }
    }

    @Override
    void notify(GridObject object) {
        if (objects.indexOf(object) == -1) {
            return;
        }
        objects.remove(object);
    }

}
