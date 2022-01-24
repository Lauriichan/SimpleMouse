package me.lauriichan.school.mouse.grid;

import java.awt.Color;

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
        for (int k = 0; k < amount.getY(); k++) {
            for (int i = 0; i < amount.getX(); i++) {
                Rectangle rect = new Rectangle();
                rect.setColor(col++ % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                rect.setWidth(size);
                rect.setHeight(size);
                rect.setX(size * i);
                rect.setY(size * k);
                pane.addChild(rect);
            }
            col++;
        }
    }

    public boolean add(GridObject object) {
        if (object == null || objects.indexOf(object) != -1) {
            return false;
        }
        object.setGrid(this);
        objects.add(object);
        return true;
    }

    @Override
    public boolean add(Stage stage, GridObject object) {
        return false;
    }
    
    public boolean remove(GridObject object) {
        boolean tmp = objects.remove(object);
        if (tmp && object instanceof GridObject) {
            object.setGrid(null);
        }
        return tmp;
    }

    @Override
    void notify(GridObject object) {
        if (objects.indexOf(object) == -1) {
            return;
        }
        objects.remove(object);
    }

}
