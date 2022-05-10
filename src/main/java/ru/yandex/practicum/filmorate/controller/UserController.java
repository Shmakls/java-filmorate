package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import javax.validation.Valid;
import java.util.*;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private Map<String, User> users = new HashMap<>();

    private UserValidator userValidator = new UserValidator();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {

        userValidator.validator(user);

        users.put(user.getEmail(), user);

        log.info("Получен запрос на создание пользователя - " + user.getEmail());

        return users.get(user.getEmail());
    }

    @PutMapping
    public void updateUser(@RequestBody User user) {

        userValidator.validator(user);

        users.put(user.getEmail(), user);

        log.info("Получен запрос на обновление пользователя - " + user.getEmail());

    }

    @GetMapping
    public List<User> findAll() {

        return new ArrayList<>(users.values());

    }

}
