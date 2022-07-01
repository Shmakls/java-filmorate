package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;


import javax.validation.constraints.Positive;
import java.util.*;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public String addNewFriend(@PathVariable int id, @PathVariable int friendId) {

        userService.addFriend(id, friendId);

        return ("Пользователи " + userService.getUserById(id).getLogin() + " и "
                + userService.getUserById(friendId).getLogin() + " добавили друг друга в друзья");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String deleteFriend(@PathVariable int id, @PathVariable int friendId) {

        userService.deleteFromFriends(id, friendId);

        return ("Пользователи " + userService.getUserById(id).getLogin() + " и "
                + userService.getUserById(friendId).getLogin() + " удалили друг друга из друзей");

    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendsList(@PathVariable int id) {

        return userService.getFriendList(id);

    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriend(@PathVariable int id, @PathVariable int otherId) {

        return userService.getCommonFriendsListAsLogins(id, otherId);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {

        userService.createUser(user);

        log.info("Получен запрос на создание пользователя - {}", user.getEmail());

        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {

        log.info("Получен запрос на обновление пользователя - {}", user.getEmail());

        return userService.updateUser(user);

    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @Positive int userId) {

        log.info("Получен запрос на удаление пользователя id = {}", userId);

        userService.deleteUser(userId);

    }

    @GetMapping
    public List<User> findAll() {

        return userService.findAll();

    }

    @GetMapping("{id}/recommendations")
    public Set<Film> filmsRecommendations(@PathVariable int id) {

        return userService.getFilmRecommendations(id);

    }

}
