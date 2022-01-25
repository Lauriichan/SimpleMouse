package me.lauriichan.school.mouse.grid;

import java.awt.Color;

import me.lauriichan.school.mouse.api.IGrid;
import me.lauriichan.school.mouse.util.BoxRenderers;
import me.lauriichan.school.mouse.util.ColorCache;
import me.lauriichan.school.mouse.util.Point;
import me.lauriichan.school.mouse.window.input.mouse.MouseButton;
import me.lauriichan.school.mouse.window.ui.DragPane;
import me.lauriichan.school.mouse.window.ui.Panel;
import me.lauriichan.school.mouse.window.ui.ResizeablePanel;
import me.lauriichan.school.mouse.window.ui.RootBar;
import me.lauriichan.school.mouse.window.ui.component.bar.BarBox;

public abstract class Grid implements IGrid {

    protected final Point amount = new Point(1, 1);
    protected final int size;

    protected final ResizeablePanel panel;
    protected final DragPane pane;

    private Runnable closingAction = null;

    public Grid(int cellAmount, int cellSize) {
        this(cellAmount, cellAmount, cellSize);
    }

    public Grid(int cellAmountX, int cellAmountY, int cellSize) {
        amount.setX(Math.max(cellAmountX, 1));
        amount.setY(Math.max(cellAmountY, 1));
        size = cellSize;

        pane = new DragPane();
        panel = new ResizeablePanel(pane);

        onSetup();
        onBarSetup();
        onGridSetup();

    }

    public void setClosingAction(Runnable closingAction) {
        this.closingAction = closingAction;
    }

    public Runnable getClosingAction() {
        return closingAction;
    }

    public abstract int getBackgroundIdx();

    protected abstract void onSetup();

    protected void onBarSetup() {
        RootBar bar = panel.getBar();
        BarBox close = bar.createBox(BoxRenderers.CROSS);
        close.setIcon(Color.GRAY, ColorCache.color("#F26161"));
        close.setIconFade(0.3, 0.15);
        close.setBox(Color.DARK_GRAY);
        close.setAction(MouseButton.LEFT, this::close);
        BarBox maximize = bar.createBox(BoxRenderers.WINDOW);
        maximize.setIcon(Color.GRAY, Color.WHITE);
        maximize.setIconFade(0.3, 0.15);
        maximize.setBox(Color.DARK_GRAY);
        maximize.setAction(MouseButton.LEFT, panel::maximize);
        BarBox minimize = bar.createBox(BoxRenderers.UNDERSCORE);
        minimize.setIcon(Color.GRAY, Color.WHITE);
        minimize.setIconFade(0.3, 0.15);
        minimize.setBox(Color.DARK_GRAY);
        minimize.setAction(MouseButton.LEFT, panel::minimize);
    }

    protected abstract void onGridSetup();

    public abstract boolean add(GridObject object);

    public abstract boolean remove(GridObject object);

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
        if (closingAction != null) {
            closingAction.run();
        }
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

    @Override
    public int getFps() {
        return panel.getFps();
    }

    @Override
    public int getTps() {
        return panel.getTps();
    }

    @Override
    public void setFps(int fps) {
        panel.setTargetFps(fps);
    }

    @Override
    public void setTps(int tps) {
        panel.setTargetTps(tps);
    }

}
