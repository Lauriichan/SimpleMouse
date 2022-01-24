package me.lauriichan.school.mouse.api.exception;

public abstract class MouseException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public MouseException(String message) {
        super(message);
    }
    
    public MouseException(String message, Throwable cause) {
        super(message, cause);
    }

}
