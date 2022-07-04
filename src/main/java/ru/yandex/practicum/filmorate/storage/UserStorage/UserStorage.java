package ru.yandex.practicum.filmorate.storage.UserStorage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {

    User save(User user);

    User update(User user);

    void delete(Integer id);

    List<User> findAll();

    User getUserById(Integer id);

    boolean contains(Integer id);

    Set<Integer> getFilmsLikeListByUser(Integer id);

    boolean isExists(Integer id);

}
