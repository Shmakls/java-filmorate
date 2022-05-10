package ru.yandex.practicum.filmorate.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {

    Film film1;
    FilmValidator filmValidator;

    @BeforeEach
    void beforeEach() {
        filmValidator = new FilmValidator();
    }

    @Test
    void validatorWorksSuccessful() {

        film1 = new Film(1, "film1", "DescriptionFilm1",
                LocalDate.of(2012, 12, 12), 5400);

        assertTrue(filmValidator.validator(film1));

    }

    @Test
    void shouldBeThrowExceptionIfNameIsEmpty() {

        film1 = new Film(1, "", "DescriptionFilm1",
                LocalDate.of(2012, 12, 12), 5400);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmValidator.validator(film1)
        );

        assertEquals("Название фильма не может быть пустым.", exception.getMessage());

    }

    @Test
    void shouldBeThrowExceptionIfDescriptionHasMore200Symbols() {

        StringBuilder sb = new StringBuilder();
        String str = "thirtySymbols";
        sb.append(str);

        while(!(sb.length() > 200)) {
            sb.append(str);
        }

        String description = sb.toString();

        film1 = new Film(1, "film1", description,
                LocalDate.of(2012, 12, 12), 5400);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmValidator.validator(film1)
        );

        assertEquals("Описание не может быть больше 200 символов", exception.getMessage());

    }

    @Test
    void shouldBeThrowExceptionIfDescriptionIsEmpty() {

        film1 = new Film(1, "film1", "",
                LocalDate.of(2012, 12, 12), 5400);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmValidator.validator(film1)
        );

        assertEquals("Описание не может быть пустым.", exception.getMessage());

    }

    @Test
    void shouldBeThrowExceptionIfReleaseDateIsBefore28Dec1895() {

        film1 = new Film(1, "film1", "DescriptionFilm1",
                LocalDate.of(1895, 12, 27), 5400);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmValidator.validator(film1)
        );

        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года.", exception.getMessage());

    }

    @Test
    void shouldBeThrowExceptionIfDurationIsNotPositive() {

        film1 = new Film(1, "film1", "DescriptionFilm1",
                LocalDate.of(2012, 12, 12), -5400);

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmValidator.validator(film1)
        );

        assertEquals("Продолжительность фильма должна быть положительной.", exception.getMessage()); //Negative test

        Film film2 = new Film(2, "film2", "DescriptionFilm2",
                LocalDate.of(2012, 12, 12), 0);

        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> filmValidator.validator(film2)
        );

        assertEquals("Продолжительность фильма должна быть положительной.", exception.getMessage()); //Zero test

    }

}