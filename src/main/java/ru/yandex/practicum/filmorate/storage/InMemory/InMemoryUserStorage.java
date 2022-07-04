package ru.yandex.practicum.filmorate.storage.InMemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Qualifier("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private Map<Integer, User> users = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {

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
    public boolean contains(Integer id) {
        return users.containsKey(id);
    }

    @Override
    public Set<Integer> getFilmsLikeListByUser(Integer id) {
        return null;
    }

    @Override
    public boolean isExists(Integer id) {
        return false;
    }


}
