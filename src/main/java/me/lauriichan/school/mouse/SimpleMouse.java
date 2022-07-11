package me.lauriichan.school.mouse;

import java.io.File;

import me.lauriichan.school.mouse.api.IGrid;
import me.lauriichan.school.mouse.api.IMouse;
import me.lauriichan.school.mouse.api.Playfield;

public class SimpleMouse extends Playfield {

    public static void main(String[] args) {
        new SimpleMouse();
    }
    
    @Override
    public IGrid onCreate() throws Exception {
        return IGrid.fromImage(new File("C:\\Users\\laura\\Desktop\\Untitled.png"));
    }
    
    @Override
    public void onStart(IGrid grid) throws Exception {
        IMouse mouse = grid.getObjects(IMouse.class)[0];
        mouse.setSpeed(1);
        for(int i = 0; i < 1000; i++) {
            mouse.turnRight();
        }
    }

}
