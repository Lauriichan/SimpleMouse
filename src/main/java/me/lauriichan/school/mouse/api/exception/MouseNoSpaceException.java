package me.lauriichan.school.mouse.api.exception;

public final class MouseNoSpaceException extends MouseException {

    private static final long serialVersionUID = -3949701077937273052L;

    public MouseNoSpaceException(String message) {
        super(message);
    }

    public MouseNoSpaceException(String message, Throwable cause) {
        super(message, cause);
    }

}
