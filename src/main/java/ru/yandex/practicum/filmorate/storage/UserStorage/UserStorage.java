package ru.yandex.practicum.filmorate.storage.UserStorage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User save(User user);

    User update(User user);

    void delete(Integer id);

    List<User> findAll();

    User getUserById(Integer id);

    boolean isContains(Integer id);

}
