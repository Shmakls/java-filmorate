package ru.yandex.practicum.filmorate.service;

import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage.UserStorage;
import ru.yandex.practicum.filmorate.validators.UserValidator;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserStorage userStorage;
    private UserValidator userValidator;

    private Integer id = 0;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
        userValidator = new UserValidator();
    }

    public User createUser(User user) {

        userValidator.isValid(user);

        user.setId(++id);

        return userStorage.add(user);

    }

    public User updateUser(User user) {

        userValidator.isValid(user);

        if (user.getId() < 1) {
            throw new IncorrectIdException("ID не может быть отрицательным");
        }

        return userStorage.update(user);

    }

    public User getUserById(Integer id) {

        if (!userStorage.isContains(id)) {
            throw new IncorrectIdException("Такого пользователя не сушествует.");
        }

        return userStorage.getUserById(id);
    }

    public void addFriend(Integer id, Integer friendId) {

        if (id < 1 || friendId < 1) {
            throw new IncorrectIdException("ID не может быть отрицательным");
        }

        User user1 = userStorage.getUserById(id);
        User user2 = userStorage.getUserById(friendId);

        Set<Integer> friends = user1.getFriends();

        if(!friends.add(user2.getId())) {
            throw new IncorrectIdException("Пользователь уже есть в друзьях");
        }

        user2.getFriends().add(user1.getId());

    }

    public void deleteFromFriends(Integer id, Integer friendId) {

        User user1 = userStorage.getUserById(id);
        User user2 = userStorage.getUserById(friendId);

        if (!user1.getFriends().remove(user2.getId())) {
            throw new IncorrectIdException("Пользователи не друзья");
        }

        user2.getFriends().remove(user1.getId());

    }

    public void deleteUser(Integer id) {

        if (!userStorage.isContains(id)) {
            throw new IncorrectIdException("Такого пользователя не сушествует.");
        }

        userStorage.delete(id);

    }

    public List<User> getFriendList(Integer id)  {

        return userStorage.getUserById(id).getFriends().stream()
                .map(friendId -> (userStorage.getUserById(friendId)))
                .collect(Collectors.toList());

    }

    public List<User> getCommonFriendsListAsLogins (Integer id, Integer otherId) {

        User user1 = userStorage.getUserById(id);
        User user2 = userStorage.getUserById(otherId);

        return Sets.intersection(user1.getFriends(), user2.getFriends()).stream()
                .map(friendsId -> (userStorage.getUserById(friendsId)))
                .collect(Collectors.toList());

    }

    public List<User> findAll() {

        return userStorage.findAll();

    }

}
