package me.lauriichan.school.mouse.window.ui.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import me.lauriichan.school.mouse.window.input.Listener;
import me.lauriichan.school.mouse.window.input.mouse.MouseClick;
import me.lauriichan.school.mouse.window.input.mouse.MouseDrag;
import me.lauriichan.school.mouse.window.input.mouse.MouseScroll;
import me.lauriichan.school.mouse.window.ui.Component;
import me.lauriichan.school.mouse.window.ui.animation.Animators;
import me.lauriichan.school.mouse.util.TimeHelper;
import me.lauriichan.school.mouse.util.Area;
import me.lauriichan.school.mouse.util.LogEntry;
import me.lauriichan.school.mouse.util.TextRender;

public class LogDisplay extends Component {

    private final ArrayList<LogEntry> history = new ArrayList<>();
    private final AtomicBoolean scrollEnabled = new AtomicBoolean(false);

    private String fontName = "Lucida Console";
    private int fontSize = 12;
    private int fontStyle = 0;

    private Color infoColor = Color.WHITE;
    private Color warnColor = Color.YELLOW;
    private Color errorColor = Color.RED;
    private Color commandColor = Color.GREEN;

    private int historySize = 200;

    private double scrollVelocity = 0;
    private double scrollMaxSpeed = 600D;
    private double scrollSpeed = 8;
    private double scrollTime = 1.0;
    private double scrollDecay = 0;
    private double scroll = 0;

    private int prevOffset = 0;

    private int lineThickness = 0;
    private Color background = Color.BLACK;
    private Color line = Color.WHITE;

    private int barWidth = 4;
    private int barHeight = 30;
    private Color barFill = Color.RED;
    private Color barBackground = Color.DARK_GRAY;

    private Consumer<LogEntry> listener;

    public void info(String line) {
        log(new LogEntry(infoColor, line, true));
    }
    
    public void warn(String line) {
        log(new LogEntry(warnColor, line, true));
    }

    public void error(String line) {
        log(new LogEntry(errorColor, line, true));
    }

    public void command(String line) {
        log(new LogEntry(commandColor, "> " + line, false));
    }

    public void append(Color color, String line) {
        log(new LogEntry(color, line, false));
    }

    public void log(Color color, String line) {
        log(new LogEntry(color, line, true));
    }

    public void log(LogEntry entry) {
        if (history.size() + 1 > historySize) {
            history.remove(historySize - 1);
        }
        history.add(0, entry);
        if (listener != null) {
            listener.accept(entry);
        }
    }

    public LogEntry remove(int index) {
        return outside(index) ? null : history.remove(index);
    }

    public void clear() {
        history.clear();
    }

    public LogEntry[] getHistory() {
        return history.toArray(new LogEntry[history.size()]);
    }

    private boolean outside(int index) {
        return index < 0 || index > history.size();
    }

    @Override
    public void render(Area area) {
        double current = scroll;
        area.fillOutline(background, lineThickness, line);
        int offset = 0;
        LogEntry[] entries = getHistory();
        int[] off = new int[entries.length];
        for (int index = 0; index < entries.length; index++) {
            int height = calcEntry(area, entries[index]);
            offset += height + fontSize;
            off[index] = height + fontSize;
        }
        boolean tmp;
        if (tmp = offset >= 400) {
            prevOffset = -(offset - 400);
            if (current < -(offset - 400)) {
                current = -(offset - 400);
                scroll = current;
                scrollVelocity = 0;
                scrollDecay = 0;
            }
            drawBar(area, Math.abs(current / (double) (offset - 400)));
            area.getGraphics().translate(0, -current);
        }
        if(tmp != scrollEnabled.get()) {
            scrollEnabled.set(tmp);
        }
        offset = 0;
        for (int index = 0; index < entries.length; index++) {
            offset += drawEntry(area, offset + (off[index] / 2), entries[index]);
        }
    }

    @Override
    public void update(long deltaTime) {
        if (!scrollEnabled.get() || scrollDecay == 0D) {
            return;
        }
        double second = TimeHelper.nanoAsSecond(deltaTime);
        scroll -= scrollVelocity * second;
        if (scroll > 0) {
            scroll = 0;
            scrollVelocity = 0;
            scrollDecay = 0;
            return;
        }
        scrollDecay -= Math.min(scrollDecay, second);
        scrollVelocity = Animators.DOUBLE.update(scrollVelocity, 0D, 1 - (scrollDecay / scrollTime));
    }

    private void drawBar(Area area, double percentage) {
        int width = area.getWidth() - barWidth - lineThickness;
        area.drawRectangle(width, lineThickness, barWidth, area.getHeight() - lineThickness, barBackground);
        area.drawRectangle(width,
            area.getHeight() - ((int) (percentage * (area.getHeight() - barHeight)) + barHeight) - (lineThickness * 2), barWidth, barHeight,
            barFill);
    }

    private int calcEntry(Area area, LogEntry entry) {
        TextRender render = area.analyseWrappedText(6, 0, entry.toString(), fontName, fontSize);
        return render.getLines() * (render.getHeight() + fontSize);
    }

    private int drawEntry(Area area, int offset, LogEntry entry) {
        TextRender render = area.drawWrappedText(6, area.getHeight() - 10 - offset, entry.toString(), entry.getColor(), fontName, fontSize,
            fontStyle);
        return render.getLines() * render.getHeight();
    }

    public void setHistorySize(int historySize) {
        this.historySize = historySize;
    }

    public int getHistorySize() {
        return historySize;
    }

    public void setListener(Consumer<LogEntry> listener) {
        this.listener = listener;
    }

    public Consumer<LogEntry> getListener() {
        return listener;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public void setInfoColor(Color infoColor) {
        this.infoColor = infoColor;
    }

    public Color getInfoColor() {
        return infoColor;
    }

    public void setErrorColor(Color errorColor) {
        this.errorColor = errorColor;
    }

    public Color getErrorColor() {
        return errorColor;
    }
    
    public void setCommandColor(Color commandColor) {
        this.commandColor = commandColor;
    }
    
    public Color getCommandColor() {
        return commandColor;
    }
    
    public void setWarnColor(Color warnColor) {
        this.warnColor = warnColor;
    }
    
    public Color getWarnColor() {
        return warnColor;
    }

    public void setLineThickness(int lineThickness) {
        this.lineThickness = lineThickness;
    }

    public int getLineThickness() {
        return lineThickness;
    }

    public void setLine(Color line) {
        this.line = line;
    }

    public Color getLine() {
        return line;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Color getBackground() {
        return background;
    }

    public void setBarBackground(Color barBackground) {
        this.barBackground = barBackground;
    }

    public Color getBarBackground() {
        return barBackground;
    }

    public void setBarFill(Color barFill) {
        this.barFill = barFill;
    }

    public Color getBarFill() {
        return barFill;
    }

    public void setBarHeight(int barHeight) {
        this.barHeight = barHeight;
    }

    public int getBarHeight() {
        return barHeight;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    public int getBarWidth() {
        return barWidth;
    }

    public void setScrollMaxSpeed(double scrollMaxSpeed) {
        this.scrollMaxSpeed = scrollMaxSpeed;
    }

    public double getScrollMaxSpeed() {
        return scrollMaxSpeed;
    }

    public void setScrollSpeed(double scrollSpeed) {
        this.scrollSpeed = scrollSpeed;
    }

    public double getScrollSpeed() {
        return scrollSpeed;
    }

    public void setScrollTime(double scrollTime) {
        this.scrollTime = scrollTime;
    }

    public double getScrollTime() {
        return scrollTime;
    }

    @Listener
    public void onDrag(MouseDrag drag) {
        if (drag.isConsumed() || !scrollEnabled.get()) {
            return;
        }
        int y = drag.getOldY();
        int by = lineThickness + getGlobalY();
        int height = getHeight() - (lineThickness * 2);
        if (by > y || by + height < y) {
            return;
        }
        int x = drag.getOldX();
        int bx = getGlobalX() - lineThickness + getWidth();
        if (bx < x || bx - barWidth > x) {
            return;
        }
        drag.consume();
        scroll = prevOffset * Math.max(0, 1 - Math.min(1, (drag.getY() - by) / (double) (height - barHeight)));
    }

    @Listener
    public void onClick(MouseClick click) {
        if (click.isConsumed() || !scrollEnabled.get()) {
            return;
        }
        int y = click.getY();
        int by = lineThickness + getGlobalY();
        int height = getHeight() - (lineThickness * 2);
        if (by > y || by + height < y) {
            return;
        }
        int x = click.getX();
        int bx = getGlobalX() - lineThickness + getWidth();
        if (bx < x || bx - barWidth > x) {
            return;
        }
        click.consume();
        scroll = prevOffset * Math.max(0, 1 - Math.min(1, (click.getY() - by) / (double) (height - barHeight)));
    }

    @Listener
    public void onScroll(MouseScroll scroll) {
        if (scroll.isConsumed() || !isInside(scroll.getX(), scroll.getY())) {
            return;
        }
        scroll.consume();
        if (!scrollEnabled.get()) {
            return;
        }
        scrollDecay = scrollTime;
        scrollVelocity = Math.max(-scrollMaxSpeed, Math.min(scrollMaxSpeed, scrollVelocity - (scroll.getScroll() * scrollSpeed)));
    }

}
