package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;

@Slf4j
public class DirectorValidator {
    public static boolean isValid(Director director) throws ValidationException {
        nameValidator(director.getName());
        return true;
    }

    private static boolean nameValidator(String name) throws ValidationException {
        if (!StringUtils.hasText(name)) {
            log.info("Имя режиссера пустое.");
            throw new ValidationException("Имя режиссера не может быть пустым.");
        }
        return true;
    }
}
