package me.lauriichan.school.mouse;

import me.lauriichan.school.mouse.grid.SimpleGrid;
import me.lauriichan.school.mouse.grid.object.Mouse;

public final class Test {

    public static void main(String[] args) {
        SimpleGrid grid = new SimpleGrid(12, 12, 128);
        Mouse mouse = new Mouse(1);
        grid.add(mouse);
        grid.center();
        grid.show();
        
        mouse.turnRight();
        mouse.move();
        mouse.move();
        mouse.move();
        
        
    }

}
