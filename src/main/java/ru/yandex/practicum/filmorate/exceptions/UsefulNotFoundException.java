package ru.yandex.practicum.filmorate.exceptions;

public class UsefulNotFoundException extends RuntimeException {
    public UsefulNotFoundException(String message) {
        super(message);
    }
}
