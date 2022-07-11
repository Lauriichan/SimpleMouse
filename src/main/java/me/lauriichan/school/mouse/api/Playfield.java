package me.lauriichan.school.mouse.api;

import java.awt.Color;

import com.syntaxphoenix.syntaxapi.utils.java.Exceptions;

//import me.lauriichan.school.mouse.grid.object.AutoMouse;
import me.lauriichan.school.mouse.util.ColorCache;
import me.lauriichan.school.mouse.window.ui.Dialog;
import me.lauriichan.school.mouse.window.ui.component.Button;
import me.lauriichan.school.mouse.window.ui.component.Label;
import me.lauriichan.school.mouse.window.ui.component.LogDisplay;
import me.lauriichan.school.mouse.window.ui.component.geometry.DragRectangle;

public abstract class Playfield {

    public Playfield() {
        IGrid grid;
        try {
            grid = onCreate();
        } catch (Exception exp) {
            openDialog(null, exp);
            return;
        }
        grid.setClosingAction(() -> System.exit(0));
        try {
            onFill(grid.getBuilder());
        } catch (Exception exp) {
            openDialog(grid, exp);
            return;
        }
        grid.center();
        grid.show();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        try {
            onStart(grid);
//            while (grid.hasObject(AutoMouse.class)) {
//                Thread.sleep(100);
//            }
        } catch (Exception exp) {
            openDialog(grid, exp);
            return;
        }
        openEndDialog(grid);
    }

    private void openDialog(IGrid grid, Exception exp) {
        Dialog dialog = new Dialog((i, panel) -> {
            panel.setSize(720, 360);
            panel.setBackground(Color.GRAY);

            DragRectangle bar = new DragRectangle();
            bar.setColor(Color.DARK_GRAY);
            bar.setHeight(64);
            bar.setWidth(panel.getWidth());
            panel.getPane().addChild(bar);

            Label info = new Label();
            info.setText("Something went wrong!");
            info.setFontSize(32);
            info.setFontColor(Color.LIGHT_GRAY);
            info.setTextCentered(true);
            info.setSize(panel.getWidth(), 48);
            info.setPosition(0, 10);
            panel.getPane().addChild(info);

            LogDisplay display = new LogDisplay();
            display.setSize(panel.getWidth() - 40, panel.getHeight() - 156);
            display.setPosition(20, 84);
            String[] array = Exceptions.stackTraceToStringArray(exp);
            Color color = ColorCache.color("F33F3F");
            for (int ix = 0; ix < array.length; ix++) {
                display.log(color, array[ix]);
            }
            panel.getPane().addChild(display);

            Button button = new Button();
            button.setHover(ColorCache.color("837C7C"), ColorCache.color("CC2222"));
            button.setHoverFade(0.2, 0.3);
            button.setShadowThickness(2);
            button.setHoverShadow(ColorCache.color("4F4A4A"), ColorCache.color("681515"));
            button.setHoverShadowFade(0.2, 0.3);
            button.setPress(Color.RED);
            button.setShadow(ColorCache.color("CC2222"));
            button.setText("Close");
            button.setTextCentered(true);
            button.setAction(() -> i.success(null));
            button.setSize(120, 32);
            button.setPosition((panel.getWidth() - button.getWidth()) / 2, panel.getHeight() - 52);
            panel.getPane().addChild(button);
        });
        dialog.show((a, b) -> {
            if (grid != null) {
                grid.close();
            }
        });
    }

    private void openEndDialog(IGrid grid) {
        Dialog dialog = new Dialog((i, panel) -> {
            panel.setSize(360, 136);
            panel.setBackground(Color.GRAY);

            DragRectangle bar = new DragRectangle();
            bar.setColor(Color.DARK_GRAY);
            bar.setHeight(64);
            bar.setWidth(panel.getWidth());
            panel.getPane().addChild(bar);

            Label info = new Label();
            info.setText("Your Mouse reached its end!");
            info.setFontSize(24);
            info.setFontColor(Color.LIGHT_GRAY);
            info.setTextCentered(true);
            info.setSize(panel.getWidth(), 48);
            info.setPosition(0, 10);
            panel.getPane().addChild(info);

            Button button = new Button();
            button.setHover(ColorCache.color("837C7C"), ColorCache.color("57EC24"));
            button.setHoverFade(0.2, 0.3);
            button.setShadowThickness(2);
            button.setHoverShadow(ColorCache.color("4F4A4A"), ColorCache.color("328B14"));
            button.setHoverShadowFade(0.2, 0.3);
            button.setPress(ColorCache.color("81E65E"));
            button.setShadow(ColorCache.color("4CA62E"));
            button.setText("Close");
            button.setTextCentered(true);
            button.setAction(() -> i.success(null));
            button.setSize(120, 32);
            button.setPosition((panel.getWidth() - button.getWidth()) / 2, panel.getHeight() - 52);
            panel.getPane().addChild(button);
        });
        dialog.show((a, b) -> {
            grid.close();
        });
    }

    public IGrid onCreate() throws Exception {
        return IGrid.newGrid(12);
    }

    public void onFill(IBuilder builder) throws Exception {};

    public abstract void onStart(IGrid grid) throws Exception;

}
