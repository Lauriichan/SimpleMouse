package me.lauriichan.school.mouse.window.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import me.lauriichan.school.mouse.window.input.Listener;
import me.lauriichan.school.mouse.window.input.mouse.MouseButton;
import me.lauriichan.school.mouse.window.input.mouse.MouseDrag;
import me.lauriichan.school.mouse.window.util.Area;

public class DragPane extends BasicPane {

    private int currentY = 0;
    private int currentX = 0;

    private int contentSizeX = 1;
    private int contentSizeY = 1;

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

    public DragPane() {
        updateBuffer();
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

    private void updateBuffer() {
        writeLock.lock();
        try {
            if (buffer != null) {
                buffer.flush();
            }
            buffer = new BufferedImage(contentSizeX, contentSizeY, BufferedImage.TYPE_INT_ARGB);
            bufferArea = new Area((Graphics2D) buffer.getGraphics(), background, -1, -1, 0, 0);
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
            super.render(bufferArea);
        } finally {
            readLock.unlock();
        }
        super.render(outputArea);
        drawBuffer(area, output);
        outputArea.clear();
        drawBars(area);
    }

    private void drawBuffer(Area area, BufferedImage buffer) {
        int width = area.getWidth();
        if (size.getY() < contentSizeY) {
            width -= barWidth;
        }
        int height = area.getHeight();
        if (size.getY() < contentSizeX) {
            height -= barWidth;
        }
        area.getGraphics().drawImage(buffer, 0, 0, width, height, currentX, currentY, currentX + width, currentY + height, null);
    }

    private void drawBars(Area area) {
        if (size.getX() < contentSizeX) {
            drawHorizontalBar(area);
        }
        if (size.getY() < contentSizeY) {
            drawVerticalBar(area);
        }
    }

    private void drawHorizontalBar(Area area) {
        // TODO: Draw horizontal bar
    }

    private void drawVerticalBar(Area area) {
        // TODO: Draw vertical bar
    }

    @Listener
    public void onDrag(MouseDrag drag) {
        if (drag.isConsumed() || drag.getButton() != MouseButton.MIDDLE) {
            return;
        }
        if (size.getX() < contentSizeX) {
            // TODO: Apply x drag
        }
        if (size.getY() < contentSizeY) {
            // TODO: Apply y drag
        }
    }

}
