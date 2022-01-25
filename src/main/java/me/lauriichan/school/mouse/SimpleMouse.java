package me.lauriichan.school.mouse;

import java.io.File;

import me.lauriichan.school.mouse.api.*;

public final class SimpleMouse extends Playfield {

    public static void main(String[] args) {
        new SimpleMouse();
    }

    @Override
    public IGrid onCreate() throws Exception {
        return IGrid.fromImage(new File("C:\\Users\\laura\\Desktop\\test.png"));
    }

    @Override
    public void onFill(IBuilder builder) throws Exception {
        if (!builder.getGrid().hasObject(IMouse.class)) {
            builder.mouseAt(0, 0, 10);
        }
    }

    @Override
    public void onStart(IGrid grid) throws Exception {
        IMouse mouse = grid.getObjects(IMouse.class)[0];
        mouse.setSpeed(12);
        
        
        
    }

}
