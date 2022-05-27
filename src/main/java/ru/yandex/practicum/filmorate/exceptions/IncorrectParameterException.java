package ru.yandex.practicum.filmorate.exceptions;

public class IncorrectParameterException extends RuntimeException {

    int parameter;

    public IncorrectParameterException(String message, int parameter) {
        super(message);
        this.parameter = parameter;
    }

    public int getParameter() {
        return parameter;
    }
}
