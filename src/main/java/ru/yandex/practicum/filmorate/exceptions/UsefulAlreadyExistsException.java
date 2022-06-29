package ru.yandex.practicum.filmorate.exceptions;

public class UsefulAlreadyExistsException extends RuntimeException {
    public UsefulAlreadyExistsException(String message) {
        super(message);
    }
}
