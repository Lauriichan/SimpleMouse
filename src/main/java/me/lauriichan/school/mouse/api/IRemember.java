package me.lauriichan.school.mouse.api;

import me.lauriichan.school.mouse.api.exception.MouseRememberException;

public interface IRemember {
    
    IMouse getOwner();
    
    void redo() throws MouseRememberException;
    
    void start() throws MouseRememberException;
    
    void stop() throws MouseRememberException;

}
