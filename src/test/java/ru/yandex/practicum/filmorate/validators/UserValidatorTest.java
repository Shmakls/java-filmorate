package ru.yandex.practicum.filmorate.validators;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class UserValidatorTest {

    User user1;
    UserValidator userValidator;

    @BeforeEach
    void beforeEach() {
        userValidator = new UserValidator();
    }

    @Test
    void validatorWorksSuccessful() throws ValidationException {

        user1 = new User("user1@ya.ru", "user1", LocalDate.of(1989, 7, 29));
        user1.setName("Пользователь1");

        assertTrue(userValidator.isValid(user1));

    }

    @Test
    void shouldBeThrowExceptionIfEmailIsEmpty() {

        user1 = new User("", "user1", LocalDate.of(1989, 7, 29));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userValidator.isValid(user1)
        );

        assertEquals("E-mail не может быть пустым.", exception.getMessage());

    }

    @Test
    void shouldBeThrowExceptionIfEmailNotHaveAt() {

        user1 = new User("user1-ya.ru", "user1", LocalDate.of(1989, 7, 29));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userValidator.isValid(user1)
        );

        assertEquals("E-mail должен содержать символ \"@\".", exception.getMessage());

    }

    @Test
    void shouldBeThrowExceptionIfLoginIsEmpty() {

        user1 = new User("user1@ya.ru", "", LocalDate.of(1989, 7, 29));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userValidator.isValid(user1)
        );

        assertEquals("Логин не может быть пустым.", exception.getMessage());

    }

    @Test
    void shouldBeThrowExceptionIfLoginHasSpaceSymbol() {

        user1 = new User("user1@ya.ru", "user 1", LocalDate.of(1989, 7, 29));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userValidator.isValid(user1)
        );

        assertEquals("Логин не может содержать пробелы.", exception.getMessage());

    }

    @Test
    void shouldBeThrowExceptionIfBirthdayIsAfterNow() {

        user1 = new User("user1@ya.ru", "user1", LocalDate.now().plusYears(1));

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userValidator.isValid(user1)
        );

        assertEquals("Дата рождения не может быть позже настоящей!", exception.getMessage());

    }

    @Test
    void shouldBeUseLoginIfNameIsEmpty() {

        user1 = new User("user1@ya.ru", "user1", LocalDate.of(1989, 7, 29));
        user1.setName("");

        userValidator.isValid(user1);

        assertEquals("user1", user1.getName());

    }

}