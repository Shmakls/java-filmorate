package ru.yandex.practicum.filmorate.service;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage.UserStorage;
import ru.yandex.practicum.filmorate.validators.UserValidator;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserStorage userStorage;
    private UserValidator userValidator;

    private FilmService filmService;

    private Integer id = 0;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage, @Lazy FilmService filmService) {
        this.userStorage = userStorage;
        userValidator = new UserValidator();
        this.filmService = filmService;
    }

    public User createUser(User user) {

        userValidator.isValid(user);

        user.setId(++id);

        return userStorage.save(user);

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

        User user1 = getUserById(id);
        User user2 = getUserById(friendId);

        Set<Integer> friends = user1.getFriends();

        if (!friends.add(user2.getId())) {
            throw new IncorrectIdException("Пользователь уже есть в друзьях");
        }

        userStorage.update(user1);

    }

    public void deleteFromFriends(Integer id, Integer friendId) {

        User user1 = getUserById(id);
        User user2 = getUserById(friendId);

        if (!user1.getFriends().remove(user2.getId())) {
            throw new IncorrectIdException("Пользователи не друзья");
        }

        userStorage.update(user1);

    }

    public void deleteUser(Integer id) {

        if (!userStorage.isContains(id)) {
            throw new IncorrectIdException("Такого пользователя не сушествует.");
        }

        userStorage.delete(id);

    }

    public List<User> getFriendList(Integer id) {

        return userStorage.getUserById(id).getFriends().stream()
                .map(friendId -> (userStorage.getUserById(friendId)))
                .collect(Collectors.toList());

    }

    public List<User> getCommonFriendsListAsLogins(Integer id, Integer otherId) {

        User user1 = userStorage.getUserById(id);
        User user2 = userStorage.getUserById(otherId);

        return Sets.intersection(user1.getFriends(), user2.getFriends()).stream()
                .map(friendsId -> (userStorage.getUserById(friendsId)))
                .collect(Collectors.toList());

    }

    public List<User> findAll() {

        return userStorage.findAll();

    }

    public Set<Film> getFilmRecommendations(int userId) {

        Set<Film> result = new HashSet<>();

        if (!userStorage.isContains(userId)) {
            throw new IncorrectIdException("Такого пользователя не существует.");
        }

        Set<Integer> likesListByUser = userStorage.getFilmsLikeListByUser(userId);

        List<User> allUsers = findAll();

        Set<Integer> allUsersId = allUsers.stream().map(User::getId).collect(Collectors.toSet());

        int intersectionAmount = 0;
        int otherUserWithMaxInterception = -1;
        Set<Integer> filmsIdToRecommend = null;

        for (Integer otherUserId : allUsersId) {

            Set<Integer> likesListByOtherUser = userStorage.getFilmsLikeListByUser(otherUserId);
            Set<Integer> intersectionList = Sets.intersection(likesListByUser, likesListByOtherUser);

            if (intersectionList.size() > intersectionAmount) {
                intersectionAmount = intersectionList.size();
                otherUserWithMaxInterception = otherUserId;

                intersectionList.forEach(likesListByOtherUser::remove);
                filmsIdToRecommend = likesListByOtherUser;

            }

        }

        if (otherUserWithMaxInterception != -1 && filmsIdToRecommend != null) {
            for (Integer filmId : filmsIdToRecommend) {

                result.add(filmService.getFilmById(filmId));

            }
        }
        return result;
    }
}
