package me.lauriichan.school.mouse.grid;

import me.lauriichan.school.mouse.api.IBlock;
import me.lauriichan.school.mouse.api.IBuilder;
import me.lauriichan.school.mouse.api.ICheese;
import me.lauriichan.school.mouse.api.IGrid;
import me.lauriichan.school.mouse.api.IMouse;
import me.lauriichan.school.mouse.grid.object.Cheese;
import me.lauriichan.school.mouse.grid.object.Mouse;
import me.lauriichan.school.mouse.grid.object.Trash;

public final class Builder implements IBuilder {

    private final Grid grid;

    public Builder(Grid grid) {
        this.grid = grid;
    }

    public IGrid getGrid() {
        return grid;
    }

    public IMouse mouseAt(int x, int y) {
        return mouseAt(x, y, 0);
    }

    public IMouse mouseAt(int x, int y, int speed) {
        Mouse mouse = new Mouse(speed);
        if (!grid.add(mouse)) {
            throw new IllegalStateException("Failed to add Mouse!");
        }
        mouse.setPosition(x, y);
        return mouse;
    }

    public ICheese cheeseAt(int x, int y) {
        return cheeseAt(x, y, 1);
    }

    public ICheese cheeseAt(int x, int y, int amount) {
        Cheese cheese = new Cheese(amount);
        if (!grid.add(cheese)) {
            throw new IllegalStateException("Failed to add Cheese!");
        }
        cheese.setPosition(x, y);
        return cheese;
    }

    public IBlock trashAt(int x, int y) {
        Trash trash = new Trash();
        if (!grid.add(trash)) {
            throw new IllegalStateException("Failed to add Trash!");
        }
        trash.setPosition(x, y);
        return trash;
    }

}
