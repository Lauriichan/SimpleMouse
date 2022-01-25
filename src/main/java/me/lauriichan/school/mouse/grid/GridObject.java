package me.lauriichan.school.mouse.grid;

import me.lauriichan.school.mouse.api.IObject;
import me.lauriichan.school.mouse.util.Point;
import me.lauriichan.school.mouse.window.ui.DragPane;

public abstract class GridObject implements IObject {

    private final Point position = new Point(-1, -1);
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
        position.setX(-1);
        position.setY(-1);
        onGridUpdate(grid);
        onRegisterComponents(grid.getBackgroundIdx(), grid.getPane());
    }
    
    protected void update(long deltaTime) {}
    
    protected boolean isUpdating() {
        return false;
    }

    protected void onGridUpdate(Grid grid) {}

    protected void onRegisterComponents(int idx, DragPane pane) {}

    protected void onUnregisterComponents(DragPane pane) {}

    public Grid getGrid() {
        return grid;
    }

    public boolean isSet() {
        return grid != null;
    }
    
    public boolean isAt(int x, int y) {
        return position.getX() == x && position.getY() == y;
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
    
    public void setPosition(int x, int y) {
        move(x, y);
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
        if(!isMoveAllowed(grid, x, y)) {
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
    
    protected boolean isMoveAllowed(Grid grid, int x, int y) {
        return true;
    }

    protected void onMoveSuccess(int x, int y) {}

    protected void onMoveFailed(int x, int y) {}

}
