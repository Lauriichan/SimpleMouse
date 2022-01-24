package me.lauriichan.school.mouse.api;

public interface IBuilder {

    IGrid getGrid();

    IMouse mouseAt(int x, int y) throws IllegalStateException;

    IMouse mouseAt(int x, int y, int speed) throws IllegalStateException;

    ICheese cheeseAt(int x, int y) throws IllegalStateException;

    ICheese cheeseAt(int x, int y, int amount) throws IllegalStateException;

    IBlock trashAt(int x, int y) throws IllegalStateException;

}
