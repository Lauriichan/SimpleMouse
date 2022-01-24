package me.lauriichan.school.mouse.api;

import me.lauriichan.school.mouse.util.Rotation;

public interface IMouse extends IObject {

    void eat();

    void move();

    void turnLeft();

    void turnRight();

    int getCheese();

    Rotation getRotation();

    IRemember getRemember();

}
