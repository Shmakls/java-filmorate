package ru.yandex.practicum.filmorate.storage.UserStorage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    public User add(User user);

    public User update(User user);

    public void delete(Integer id);

    public List<User> findAll();

    public User getUserById(Integer id);

    public boolean isContains(Integer id);

}
