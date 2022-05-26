package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Context;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;


import java.util.*;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private UserService userService = Context.USER_SERVICE;

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

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }

    @GetMapping
    public List<User> findAll() {

        return userService.findAll();

    }

}
