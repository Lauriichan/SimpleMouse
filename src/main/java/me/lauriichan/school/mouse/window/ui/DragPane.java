package me.lauriichan.school.mouse.window.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import me.lauriichan.school.mouse.util.Area;
import me.lauriichan.school.mouse.util.DynamicArray;
import me.lauriichan.school.mouse.window.input.Listener;
import me.lauriichan.school.mouse.window.input.mouse.MouseButton;
import me.lauriichan.school.mouse.window.input.mouse.MouseDrag;
import me.lauriichan.school.mouse.window.input.mouse.MousePress;
import me.lauriichan.school.mouse.window.input.mouse.MouseScroll;

public class DragPane extends Pane {

    private int currentY = 0;
    private int currentX = 0;

    private int contentSizeX = 1;
    private int contentSizeY = 1;

    private float zoom = 1f;
    private float zoomSpeed = 0.01f;

    private float horizontalMoveSpeed = 1f;
    private float verticalMoveSpeed = 1f;

    private int borderBuffer = 50;

    private Color background = Color.BLACK;

    private int barWidth = 4;
    private int barLength = 30;
    private Color barFill = Color.RED;
    private Color barBackground = Color.DARK_GRAY;

    private BufferedImage buffer;
    private Area bufferArea;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock writeLock = lock.writeLock();
    private final Lock readLock = lock.readLock();

    private final DynamicArray<Component> components = new DynamicArray<>();

    public DragPane() {
        updateBuffer();
    }

    @Override
    public boolean addChild(Component component) {
        if (components.indexOf(component) != -1) {
            return false;
        }
        components.add(component);
        return true;
    }

    public boolean addChildBack(Component component) {
        if (components.indexOf(component) != -1) {
            return false;
        }
        components.addBack(component);
        return true;
    }

    @Override
    public boolean removeChild(Component component) {
        int index = components.indexOf(component);
        if (index == -1) {
            return false;
        }
        components.remove(index);
        return true;
    }

    @Override
    public int getChildrenCount() {
        return components.length();
    }

    @Override
    public Component getChild(int index) {
        return components.get(index);
    }

    @Override
    public Component[] getChildren() {
        return components.asArray(Component[]::new);
    }

    public int getContentSizeX() {
        return contentSizeX;
    }

    public int getContentSizeY() {
        return contentSizeY;
    }

    public void setContentSizeX(int contentSizeX) {
        this.contentSizeX = Math.max(contentSizeX, 1);
        updateBuffer();
    }

    public void setContentSizeY(int contentSizeY) {
        this.contentSizeY = Math.max(contentSizeY, 1);
        updateBuffer();
    }

    public void center() {
        currentX = (int) Math.max(((contentSizeX * zoom) - size.getX()) / 2, 0);
        currentY = (int) Math.max(((contentSizeY * zoom) - size.getY()) / 2, 0);
    }

    public void centerZoom() {
        float tmpX = size.getX() / (float) (contentSizeX + borderBuffer);
        float tmpY = size.getY() / (float) (contentSizeY + borderBuffer);
        zoom = Math.min(tmpX, tmpY);
        center();
    }

    private void updateBuffer() {
        writeLock.lock();
        try {
            if (buffer != null) {
                buffer.flush();
            }
            buffer = new BufferedImage(contentSizeX, contentSizeY, BufferedImage.TYPE_INT_ARGB);
            bufferArea = new Area((Graphics2D) buffer.getGraphics(), background, -1, -1, contentSizeX, contentSizeY);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void render(Area area) {
        Area outputArea;
        BufferedImage output;
        readLock.lock();
        try {
            output = buffer;
            outputArea = bufferArea;
            renderChildren(bufferArea);
        } finally {
            readLock.unlock();
        }
        super.render(outputArea);
        drawBuffer(area, output);
        outputArea.clear();
        drawBars(area);
    }

    private void renderChildren(Area area) {
        Component[] children = getChildren();
        for (int i = 0; i < children.length; i++) {
            Component component = children[i];
            if (component.isHidden()) {
                continue;
            }
            component.render(area.create(component.getX(), component.getY(), component.getWidth(), component.getHeight()));
        }
    }

    @Override
    public void update(long deltaTime) {
        Component[] children = getChildren();
        for (int i = 0; i < children.length; i++) {
            Component component = children[i];
            if (!component.isUpdating()) {
                continue;
            }
            component.update(deltaTime);
        }
    }

    private void drawBuffer(Area area, BufferedImage buffer) {
        int width = area.getWidth();
        if (size.getY() < (contentSizeY * zoom)) {
            width -= barWidth;
        }
        int height = area.getHeight();
        if (size.getX() < (contentSizeX * zoom)) {
            height -= barWidth;
        }
        Image image = buffer;
        int iWidth = buffer.getWidth();
        int iHeight = buffer.getHeight();
        if (zoom != 1) {
            iWidth *= zoom;
            iHeight *= zoom;
            image = buffer.getScaledInstance((int) (buffer.getWidth() * zoom), (int) (buffer.getHeight() * zoom), Image.SCALE_FAST);
        }
        int ix = size.getX() >= (contentSizeX * zoom) ? (int) (((area.getWidth() - iWidth) / 2)) : 0;
        int iy = size.getY() >= (contentSizeY * zoom) ? (int) (((area.getHeight() - iHeight) / 2)) : 0;
        area.getGraphics().drawImage(image, ix, iy, ix + width, iy + height, currentX, currentY, currentX + width, currentY + height, null);
    }

    private void drawBars(Area area) {
        if (size.getX() < (contentSizeX * zoom)) {
            drawHorizontalBar(area);
        }
        if (size.getY() < (contentSizeY * zoom)) {
            drawVerticalBar(area);
        }
    }

    private void drawHorizontalBar(Area area) {
        float max = (float) Math.floor((contentSizeX * zoom) - size.getX());
        area.drawRectangle(0, size.getY() - barWidth, size.getX(), barWidth, barBackground);
        float progress = (currentX / max);
        int extra = size.getY() < (contentSizeY * zoom) ? barWidth : 0;
        area.drawRectangle((int) (progress * (area.getWidth() - barLength - extra)), size.getY() - barWidth, barLength, barWidth, barFill);
    }

    private void drawVerticalBar(Area area) {
        float max = (float) Math.floor((contentSizeY * zoom) - size.getY());
        area.drawRectangle(size.getX() - barWidth, 0, barWidth, size.getY(), barBackground);
        float progress = (currentY / max);
        int extra = size.getX() < (contentSizeX * zoom) ? barWidth : 0;
        area.drawRectangle(size.getX() - barWidth, (int) (progress * (area.getHeight() - barLength - extra)), barWidth, barLength, barFill);
    }

    private int moveX;
    private int moveY;

    @Listener
    public void onScroll(MouseScroll scroll) {
        if (scroll.isConsumed() || !isInside(scroll.getX(), scroll.getY())) {
            return;
        }
        float zoomBuf = zoom;
        zoomBuf -= (scroll.getRotation() * zoomSpeed);
        int widthBuf = (int) (buffer.getWidth() * zoomBuf);
        int heightBuf = (int) (buffer.getHeight() * zoomBuf);
        if (widthBuf < 1 || widthBuf > (contentSizeX) || heightBuf < 1 || heightBuf > (contentSizeY)) {
            return;
        }
        float buf = zoom;
        zoom = zoomBuf;
        scroll.consume();
        if (buf > zoomBuf) {
            int tmp = (int) Math.floor(contentSizeX * zoom) - size.getX();
            if (tmp > 0 && (currentX * 2f) > tmp) {
                currentX = tmp;
            } else if (tmp <= 0) {
                currentX = 0;
            }
            tmp = (int) Math.floor(contentSizeY * zoom) - size.getY();
            if (tmp > 0 && (currentY * 2f) > tmp) {
                currentY = tmp;
            } else if (tmp <= 0) {
                currentY = 0;
            }
        }
    }

    @Listener
    public void onDrag(MouseDrag drag) {
        if (drag.isConsumed() || drag.getButton() != MouseButton.MIDDLE || (moveX == -1 && moveY == -1)) {
            return;
        }
        if (size.getX() < (contentSizeX * zoom)) {
            int max = (int) Math.floor((contentSizeX * zoom) - size.getX());
            int move = moveX - drag.getX();
            int x = currentX + (int) (move * horizontalMoveSpeed);
            if (x < 0) {
                x = 0;
            } else if (x > max) {
                x = max;
            }
            currentX = x;
            moveX = drag.getX();
        }
        if (size.getY() < (contentSizeY * zoom)) {
            int max = (int) Math.floor((contentSizeY * zoom) - size.getY());
            int move = moveY - drag.getY();
            int y = currentY + (int) (move * verticalMoveSpeed);
            if (y < 0) {
                y = 0;
            } else if (y > max) {
                y = max;
            }
            currentY = y;
            moveY = drag.getY();
        }
        drag.consume();
    }

    @Listener
    public void onPress(MousePress press) {
        if (!isInside(press.getX(), press.getY())) {
            moveX = -1;
            moveY = -1;
            return;
        }
        moveX = press.getX();
        moveY = press.getY();
    }

    @Override
    public void setBar(Bar<?> bar) {}

    @Override
    public Bar<?> getBar() {
        return null;
    }

    @Override
    public boolean hasBar() {
        return false;
    }

}
