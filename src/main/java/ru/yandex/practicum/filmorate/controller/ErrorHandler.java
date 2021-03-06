package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.*;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationExceptionHandler(final ValidationException e) {

        return new ErrorResponse("error:" + e.getMessage());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse incorrectIdExceptionHandler(final IncorrectIdException e) {

        return new ErrorResponse("error:" + e.getMessage());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse IncorrectParameterExceptionHandler(final IncorrectParameterException e) {

        return new ErrorResponse("Ошибка с полем: " + e.getParameter());

    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse controllerValidateHandler(ConstraintViolationException exp) {
        return new ErrorResponse("Проверьте передаваемые значения. Они должны существовать и быть больше 0");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleReviewNotFound(final ReviewNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }
}
