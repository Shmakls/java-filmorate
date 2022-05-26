package ru.yandex.practicum.filmorate.exceptions;

public class IncorrectIdException extends RuntimeException {

    public IncorrectIdException(String message) {
        super(message);
    }
}
