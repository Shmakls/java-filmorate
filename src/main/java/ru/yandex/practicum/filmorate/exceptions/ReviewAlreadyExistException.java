package ru.yandex.practicum.filmorate.exceptions;

public class ReviewAlreadyExistException extends RuntimeException {
    public ReviewAlreadyExistException(String message) {
        super(message);
    }
}
