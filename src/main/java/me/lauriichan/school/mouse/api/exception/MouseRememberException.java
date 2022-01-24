package me.lauriichan.school.mouse.api.exception;

public final class MouseRememberException extends MouseException {

    private static final long serialVersionUID = -3401614044005122086L;

    public MouseRememberException(String message) {
        super(message);
    }

    public MouseRememberException(String message, Throwable cause) {
        super(message, cause);
    }

}
