package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.*;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationExceptionHandler(final ValidationException e) {

        return new ErrorResponse("error:" + e.getMessage());

    }

    @ExceptionHandler({
            FilmNotFoundException.class,
            IncorrectIdException.class,
            ReviewNotFoundException.class,
            UsefulNotFoundException.class,
            UserNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse incorrectIdExceptionHandler(RuntimeException e) {
        return new ErrorResponse(e.getMessage());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse IncorrectParameterExceptionHandler(final IncorrectParameterException e) {

        return new ErrorResponse("Ошибка с полем: " + e.getParameter());

    }

}
