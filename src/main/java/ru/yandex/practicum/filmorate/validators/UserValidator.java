package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidator {

    public boolean validator(User user) {

        emailValidator(user.getEmail());

        loginValidator(user.getLogin());

        BirthdayValidator(user.getBirthday());

        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.info("Имя для отображения пустое, будет использован логин.");
        }

        return true;

    }

    private boolean emailValidator(String email) {

        if (email.isEmpty()) {
            log.info("Мыло пустое.");
            throw new ValidationException("E-mail не может быть пустым.");
        }

        if (!email.contains("@")) {
            log.info("Неправильный формат мыла.");
            throw new ValidationException("E-mail должен содержать символ \"@\".");
        }

        return true;
    }

    private boolean loginValidator(String login)  {

        if (login.isEmpty()) {
            log.info("Пустой логин.");
            throw new ValidationException("Логин не может быть пустым.");
        }

        if (login.contains(" ")) {
            log.info("Логин с пробелами.");
            throw new ValidationException("Логин не может содержать пробелы.");
        }

        return true;

    }

    private boolean BirthdayValidator(LocalDate birthdate) {

        if (birthdate.isAfter(LocalDate.now())) {
            log.info("Дата рождения позже текущей.");
            throw new ValidationException("Дата рождения не может быть позже настоящей!");
        }

        return true;

    }

}
