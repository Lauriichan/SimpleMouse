package me.lauriichan.school.mouse.window.ui;

import java.awt.Frame;

import me.lauriichan.school.mouse.window.input.Listener;
import me.lauriichan.school.mouse.window.input.mouse.MouseButton;
import me.lauriichan.school.mouse.window.input.mouse.MouseDrag;
import me.lauriichan.school.mouse.window.input.mouse.MousePress;
import me.lauriichan.school.mouse.window.input.mouse.MouseRelease;

public class ResizeablePanel extends Panel {

    public ResizeablePanel() {
        super();
        init(frame);
    }

    public ResizeablePanel(Pane pane) {
        super(pane);
        init(frame);
    }

    public ResizeablePanel(RootBar bar) {
        super(bar);
        init(frame);
    }

    public ResizeablePanel(RootBar bar, Pane pane) {
        super(bar, pane);
        init(frame);
    }

    private void init(Frame frame) {
        frame.setResizable(true);
    }

    private int x, y;
    private ResizeState state = null;

    @Listener
    public void onPress(MousePress press) {
        if (press.isConsumed() || press.getButton() != MouseButton.LEFT) {
            return;
        }
        ResizeState tmp = ResizeState.of(size.getX(), size.getY(), press.getX(), press.getY());
        if (tmp == null) {
            return;
        }
        x = press.getScreenX();
        y = press.getScreenY();
        state = tmp;
    }

    @Listener
    public void onRelease(MouseRelease release) {
        if (state == null || release.getButton() != MouseButton.LEFT) {
            return;
        }
        state = null;
    }

    @Listener
    public void onDrag(MouseDrag drag) {
        ResizeState tmp = state;
        if (tmp == null || drag.isConsumed() || drag.getButton() != MouseButton.LEFT) {
            return;
        }
        int difX = drag.getScreenX() - x;
        int difY = drag.getScreenY() - y;
        switch (state) {
        case RIGHT:
            if (difX == 0) {
                return;
            }
            setWidth(size.getX() + difX);
            break;
        case LEFT:
            if (difX == 0) {
                return;
            }
            setWidth(size.getX() - difX);
            updatePosition(drag.getX() - drag.getOldX(), getY());
            break;
        case BOTTOM:
            if (difY == 0) {
                return;
            }
            setHeight(size.getY() + difY);
            break;
        case TOP:
            if (difY == 0) {
                return;
            }
            setHeight(size.getY() - difY);
            updatePosition(getX(), drag.getY() - drag.getOldY());
            break;
        case TOP_LEFT:
            if (difY == 0 && difX == 0) {
                return;
            }
            setWidth(size.getX() - difX);
            setHeight(size.getY() - difY);
            updatePosition(drag.getX() - drag.getOldX(), drag.getY() - drag.getOldY());
            break;
        case TOP_RIGHT:
            if (difY == 0 && difX == 0) {
                return;
            }
            setWidth(size.getX() + difX);
            setHeight(size.getY() - difY);
            updatePosition(getX(), drag.getY() - drag.getOldY());
            break;
        case BOTTOM_LEFT:
            if (difY == 0 && difX == 0) {
                return;
            }
            setWidth(size.getX() - difX);
            setHeight(size.getY() + difY);
            updatePosition(getX(), drag.getY() - drag.getOldY());
            break;
        case BOTTOM_RIGHT:
            if (difY == 0 && difX == 0) {
                return;
            }
            setWidth(size.getX() + difX);
            setHeight(size.getY() + difY);
            break;
        }
        drag.consume();
        x = drag.getScreenX();
        y = drag.getScreenY();
    }

    private void updatePosition(int x, int y) {
        if (x != getX()) {
            setX(getX() + x);
        }
        if (y != getY()) {
            setY(getY() + y);
        }
    }

    static enum ResizeState {

        TOP(false, true, false, false),
        TOP_RIGHT(false, true, true, false),
        RIGHT(false, false, true, false),
        BOTTOM_RIGHT(false, false, true, true),
        BOTTOM(false, false, false, true),
        BOTTOM_LEFT(true, false, false, true),
        LEFT(true, false, false, false),
        TOP_LEFT(true, true, false, false);

        private final boolean[] states;

        private ResizeState(boolean left, boolean top, boolean right, boolean bottom) {
            this.states = new boolean[] {
                left,
                top,
                right,
                bottom
            };
        }

        public boolean isLeft() {
            return states[0];
        }

        public boolean isTop() {
            return states[1];
        }

        public boolean isRight() {
            return states[2];
        }

        public boolean isBottom() {
            return states[3];
        }

        private boolean match(boolean[] target) {
            for (int i = 0; i < states.length; i++) {
                if (states[i] != target[i]) {
                    return false;
                }
            }
            return true;
        }

        public static ResizeState of(int width, int height, int x, int y) {
            boolean[] states = new boolean[] {
                x < 10,
                y < 10,
                x > width - 10,
                y > height - 10
            };
            for (ResizeState state : ResizeState.values()) {
                if (state.match(states)) {
                    return state;
                }
            }
            return null;
        }

    }

}
