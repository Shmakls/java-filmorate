package ru.yandex.practicum.filmorate.exceptions;

public class LikeRecordAlreadyExistsException extends RuntimeException {
    public LikeRecordAlreadyExistsException(String message) {
        super(message);
    }
}
