package me.lauriichan.school.mouse.grid.object;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import me.lauriichan.school.mouse.api.IBlock;
import me.lauriichan.school.mouse.api.IMouse;
import me.lauriichan.school.mouse.api.IRemember;
import me.lauriichan.school.mouse.api.exception.MouseException;
import me.lauriichan.school.mouse.api.exception.MouseNoCheeseException;
import me.lauriichan.school.mouse.api.exception.MouseNoSpaceException;
import me.lauriichan.school.mouse.api.exception.MouseRememberException;
import me.lauriichan.school.mouse.grid.Grid;
import me.lauriichan.school.mouse.grid.GridObject;
import me.lauriichan.school.mouse.grid.GridSprite;
import me.lauriichan.school.mouse.util.ImageCache;
import me.lauriichan.school.mouse.util.Rotation;
import me.lauriichan.school.mouse.window.ui.DragPane;

public final class Mouse extends GridObject implements IMouse {

    private static final BufferedImage MOUSE_IMAGE = ImageCache.resource("mouse", "images/mouse.png");

    private final GridSprite sprite = new GridSprite(this, MOUSE_IMAGE);

    private final Runnable exeEat = this::eat;
    private final Runnable exeMove = this::move;
    private final Runnable exeLeft = this::turnLeft;
    private final Runnable exeRight = this::turnRight;

    private final ArrayList<MouseRemember> remembers = new ArrayList<>();

    private final ReentrantLock lock = new ReentrantLock();

    private long wait = 1000;
    private int speed = 1;
    
    private int cheese = 0;

    public Mouse(int speed) {
        setSpeed(speed);
    }

    @Override
    public void setSpeed(int speed) {
        this.wait = (1000 / (this.speed = Math.min(Math.max(speed, 1), 20)));
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    public GridSprite getSprite() {
        return sprite;
    }

    @Override
    protected void onRegisterComponents(DragPane pane) {
        pane.addChild(sprite);
    }

    @Override
    protected void onUnregisterComponents(DragPane pane) {
        pane.removeChild(sprite);
    }

    @Override
    public void eat() {
        lock.lock();
        try {
            action(exeEat);
            internalEat();
            sleep();
        } finally {
            lock.unlock();
        }
    }
    
    private void internalEat() {
        Cheese[] cheeses = getGrid().getObjectsAt(getX(), getY(), Cheese.class);
        if(cheeses.length == 0) {
            throw new MouseNoCheeseException(String.format("There is no cheese at (%s, %s)", getX(), getY()));
        }
        for(int i = 0; i < cheeses.length; i++) {
            Cheese cheese = cheeses[i];
            if(cheese.getAmount() != 0) {
                cheese.setAmount(cheese.getAmount() - 1);
                return;
            }
        }
        throw new MouseNoCheeseException(String.format("There is no cheese at (%s, %s)", getX(), getY()));
    }

    @Override
    public void move() {
        lock.lock();
        try {
            action(exeMove);
            internalMove();
            sleep();
        } finally {
            lock.unlock();
        }
    }

    private void internalMove() {
        switch (getRotation()) {
        case NORTH:
            setY(getY() - 1);
            break;
        case EAST:
            setX(getX() + 1);
            break;
        case SOUTH:
            setY(getY() + 1);
            break;
        case WEST:
            setX(getX() - 1);
            break;
        }
    }

    @Override
    protected boolean isMoveAllowed(Grid grid, int x, int y) {
        return !grid.hasObjectAt(x, y, IBlock.class);
    }

    @Override
    protected void onMoveSuccess(int x, int y) {
        sprite.setX(asGridPosition(x));
        sprite.setY(asGridPosition(y));
    }

    @Override
    protected void onMoveFailed(int x, int y) {
        throw new MouseNoSpaceException(String.format("Can't move to (%s, %s)", x, y));
    }

    @Override
    public void turnLeft() {
        lock.lock();
        try {
            action(exeLeft);
            sprite.setRotation(sprite.getRotation().left());
            sleep();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void turnRight() {
        lock.lock();
        try {
            action(exeRight);
            sprite.setRotation(sprite.getRotation().right());
            sleep();
        } finally {
            lock.unlock();
        }
    }

    private void sleep() {
        if (!getGrid().getPanel().isRunning()) {
            return;
        }
        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {
        }
    }

    private void action(Runnable action) {
        if (remembers.isEmpty()) {
            return;
        }
        try {
            for (int i = 0; i < remembers.size(); i++) {
                remembers.get(i).notify(action);
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException ignore) {
        }
    }

    @Override
    public int getCheese() {
        return cheese;
    }

    @Override
    public Rotation getRotation() {
        return sprite.getRotation();
    }

    @Override
    public IRemember getRemember() {
        return new MouseRemember(this);
    }

    class MouseRemember implements IRemember {

        private final Mouse owner;

        private ArrayList<Runnable> actions = new ArrayList<>();
        private int size = 0;
        private boolean redoing = false;

        public MouseRemember(Mouse owner) {
            this.owner = owner;
        }

        void notify(Runnable action) {
            if (size != -1) {
                return;
            }
            actions.add(action);
        }

        @Override
        public IMouse getOwner() {
            return owner;
        }

        @Override
        public void redo() {
            if (size == 0) {
                throw new MouseRememberException("Can't repeat actions if no actions were learned");
            }
            if (size == -1) {
                throw new MouseRememberException("Can't redo actions while learning, use stop() before using redo()");
            }
            if (redoing) {
                throw new MouseRememberException("Already repeating learned actions");
            }
            redoing = true;
            try {
                for (int i = 0; i < size; i++) {
                    actions.get(i).run();
                }
            } catch (MouseException exp) {
                throw new MouseRememberException("Failed to execute learned actions", exp);
            }
            redoing = false;
        }

        @Override
        public void start() {
            if (redoing) {
                throw new MouseRememberException("Currently repeating learned actions");
            }
            if (size == -1) {
                throw new MouseRememberException("Already learning actions");
            }
            actions.clear();
            owner.remembers.add(this);
            size = -1;
        }

        @Override
        public void stop() {
            if (size != -1) {
                throw new MouseRememberException("Currently not learning");
            }
            owner.remembers.remove(this);
            size = actions.size();
        }

    }

}
