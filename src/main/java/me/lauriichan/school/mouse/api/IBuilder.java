package me.lauriichan.school.mouse.api;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

public interface IBuilder {

    IGrid getGrid();

    IMouse mouseAt(int x, int y) throws IllegalStateException;

    IMouse mouseAt(int x, int y, int speed) throws IllegalStateException;
    
    IMouse mouseAt(int x, int y, Color color) throws IllegalStateException;
    
    IMouse mouseAt(int x, int y, int speed, Color color) throws IllegalStateException;
    
    IMouse mouseRandomAt(int x, int y, int speed) throws IllegalStateException;

    ICheese cheeseAt(int x, int y) throws IllegalStateException;

    ICheese cheeseAt(int x, int y, int amount) throws IllegalStateException;

    IBlock trashAt(int x, int y) throws IllegalStateException;

    void loadImage(File file) throws IllegalStateException;

    void loadImage(BufferedImage image) throws IllegalStateException;

}
