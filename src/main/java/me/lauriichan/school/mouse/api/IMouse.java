package me.lauriichan.school.mouse.api;

import me.lauriichan.school.mouse.api.exception.MouseNoCheeseException;
import me.lauriichan.school.mouse.api.exception.MouseNoSpaceException;
import me.lauriichan.school.mouse.util.Rotation;

public interface IMouse extends IObject {

    void eat() throws MouseNoCheeseException;

    void move() throws MouseNoSpaceException;
    
    boolean smellsCheese();
    
    boolean canMove();
    
    void turnLeft();

    void turnRight();

    int getCheese();
    
    int getSpeed();
    
    void setSpeed(int speed);

    Rotation getRotation();

    IRemember getRemember();

}
