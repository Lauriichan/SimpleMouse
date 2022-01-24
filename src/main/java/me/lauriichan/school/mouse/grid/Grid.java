package me.lauriichan.school.mouse.grid;

import java.awt.Color;

import me.lauriichan.school.mouse.api.IGrid;
import me.lauriichan.school.mouse.util.BoxRenderers;
import me.lauriichan.school.mouse.util.ColorCache;
import me.lauriichan.school.mouse.util.Point;
import me.lauriichan.school.mouse.window.input.mouse.MouseButton;
import me.lauriichan.school.mouse.window.ui.DragPane;
import me.lauriichan.school.mouse.window.ui.Panel;
import me.lauriichan.school.mouse.window.ui.RootBar;
import me.lauriichan.school.mouse.window.ui.component.bar.BarBox;

public abstract class Grid implements IGrid {

    protected final Point amount = new Point(1, 1);
    protected final int size;

    protected final Panel panel;
    protected final DragPane pane;

    public Grid(int cellAmount, int cellSize) {
        this(cellAmount, cellAmount, cellSize);
    }

    public Grid(int cellAmountX, int cellAmountY, int cellSize) {
        amount.setX(Math.max(cellAmountX, 1));
        amount.setY(Math.max(cellAmountY, 1));
        size = cellSize;

        pane = new DragPane();
        panel = new Panel(pane);

        onSetup();
        onBarSetup();
        onGridSetup();

    }

    protected abstract void onSetup();

    protected void onBarSetup() {
        RootBar bar = panel.getBar();
        BarBox close = bar.createBox(BoxRenderers.CROSS);
        close.setIcon(Color.GRAY, ColorCache.color("#F26161"));
        close.setIconFade(0.3, 0.15);
        close.setBox(Color.DARK_GRAY);
        close.setAction(MouseButton.LEFT, this::close);
        BarBox minimize = bar.createBox(BoxRenderers.UNDERSCORE);
        minimize.setIcon(Color.GRAY, Color.WHITE);
        minimize.setIconFade(0.3, 0.15);
        minimize.setBox(Color.DARK_GRAY);
        minimize.setAction(MouseButton.LEFT, panel::minimize);
    }

    protected abstract void onGridSetup();
    
    public abstract boolean add(Stage stage, GridObject object);
    
    abstract void notify(GridObject object);

    public Panel getPanel() {
        return panel;
    }

    public DragPane getPane() {
        return pane;
    }

    public int getWidth() {
        return amount.getX();
    }

    public int getHeight() {
        return amount.getY();
    }

    public int getCellSize() {
        return size;
    }

    public void close() {
        if (!panel.isRunning()) {
            return;
        }
        panel.exit();
    }

    public void center() {
        pane.centerZoom();
        panel.center();
    }

    public void show() {
        panel.show();
    }

    public void hide() {
        panel.hide();
    }

    public boolean isHidden() {
        return panel.isHidden();
    }

}
