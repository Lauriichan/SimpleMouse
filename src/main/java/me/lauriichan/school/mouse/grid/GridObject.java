package me.lauriichan.school.mouse.grid;

import me.lauriichan.school.mouse.util.Point;
import me.lauriichan.school.mouse.window.ui.Pane;

public abstract class GridObject {

    private final Point position = new Point(0, 0);
    private Grid grid;

    void setGrid(Grid grid) {
        if(this.grid == grid) {
            return;
        }
        if (this.grid != null) {
            onUnregisterComponents(this.grid.getPane());
            this.grid.notify(this);
        }
        this.grid = grid;
        if (grid == null) {
            return;
        }
        if (grid.getWidth() < position.getX()) {
            position.setX(grid.getWidth() - 1);
        }
        if (grid.getHeight() < position.getY()) {
            position.setY(grid.getHeight() - 1);
        }
        onGridUpdate(grid);
        onRegisterComponents(grid.getPane());
    }
    
    protected void update(long deltaTime) {}
    
    protected boolean isUpdating() {
        return false;
    }

    protected void onGridUpdate(Grid grid) {}

    protected void onRegisterComponents(Pane pane) {}

    protected void onUnregisterComponents(Pane pane) {}

    public Grid getGrid() {
        return grid;
    }

    public boolean isSet() {
        return grid != null;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public void setX(int x) {
        move(x, position.getY());
    }

    public void setY(int y) {
        move(position.getX(), y);
    }

    protected void move(int x, int y) {
        if(grid == null) {
            return;
        }
        if(x < 0 || x >= grid.getWidth()) {
            onMoveFailed(x, y);
            return;
        }
        if(y < 0 || y >= grid.getHeight()) {
            onMoveFailed(x, y);
            return;
        }
        position.setX(x);
        position.setY(y);
        onMoveSuccess(x, y);
    }
    
    protected final int asGridPosition(int coordinate) {
        return coordinate * grid.getCellSize();
    }

    protected void onMoveSuccess(int x, int y) {}

    protected void onMoveFailed(int x, int y) {}

}
