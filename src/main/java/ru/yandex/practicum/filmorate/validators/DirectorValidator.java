package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;

@Slf4j
@Service
public class DirectorValidator {
    public boolean isValid(Director director) throws ValidationException {
        nameValidator(director.getName());
        return true;
    }

    private boolean nameValidator(String name) throws ValidationException {
        if (!StringUtils.hasText(name)) {
            log.info("Имя режиссера пустое.");
            throw new ValidationException("Имя режиссера не может быть пустым.");
        }
        return true;
    }
}
