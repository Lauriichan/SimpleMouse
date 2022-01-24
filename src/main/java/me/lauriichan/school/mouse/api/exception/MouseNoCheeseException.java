package me.lauriichan.school.mouse.api.exception;

public final class MouseNoCheeseException extends MouseException {

    private static final long serialVersionUID = -6706640867345310694L;

    public MouseNoCheeseException(String message) {
        super(message);
    }

    public MouseNoCheeseException(String message, Throwable cause) {
        super(message, cause);
    }

}
