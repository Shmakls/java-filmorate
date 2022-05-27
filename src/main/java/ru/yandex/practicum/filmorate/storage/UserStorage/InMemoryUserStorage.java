package ru.yandex.practicum.filmorate.storage.UserStorage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Integer, User> users = new ConcurrentHashMap<>();

    @Override
    public User add(User user) {

        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User update(User user) {

        users.put(user.getId(), user);

        return user;
    }

    @Override
    public void delete(Integer id) {

        users.remove(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Integer id) {
        return users.get(id);
    }

    @Override
    public boolean isContains(Integer id) {
        return users.containsKey(id);
    }
}
