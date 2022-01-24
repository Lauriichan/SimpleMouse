package me.lauriichan.school.mouse;

import me.lauriichan.school.mouse.api.*;

public final class SimpleMouse extends Playfield {

    public static void main(String[] args) {
        new SimpleMouse();
    }

    @Override
    public void onCreate(IBuilder builder) throws Exception {
        builder.mouseAt(5, 5, 10);
        
        builder.trashAt(6, 6);
        
    }

    @Override
    public void onStart(IGrid grid) throws Exception {
        IMouse mouse = grid.getObjects(IMouse.class)[0];
        
        mouse.move();
        
    }

}
