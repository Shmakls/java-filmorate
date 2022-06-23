package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.dao.FriendsListDao;
import ru.yandex.practicum.filmorate.storage.db.dao.SubscribesListDao;
import ru.yandex.practicum.filmorate.storage.db.dao.UsersDao;

import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private JdbcTemplate jdbcTemplate;
    private UsersDao usersDao;
    private FriendsListDao friendsListDao;
    private SubscribesListDao subscribersListDao;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, UsersDao usersDao, FriendsListDao friendsListDao, SubscribesListDao subscribersListDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.usersDao = usersDao;
        this.friendsListDao = friendsListDao;
        this.subscribersListDao = subscribersListDao;
    }

    @Override
    public User save(User user) {

        User userWithId = usersDao.save(user);

        friendsListDao.saveFriendList(userWithId);
        subscribersListDao.saveSubscribesList(userWithId);

        return userWithId;

    }

    @Override
    public User update(User user) {

        if (isContains(user.getId())) {

            String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";

            friendsListDao.delete(user.getId());
            friendsListDao.saveFriendList(user);

            subscribersListDao.delete(user.getId());
            subscribersListDao.saveSubscribesList(user);

            jdbcTemplate.update(sql, user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());

        } else {
            save(user);
        }

        return user;

    }

    @Override
    public void delete(Integer id) {

        friendsListDao.delete(id);

        subscribersListDao.delete(id);

        usersDao.deleteUserById(id);

    }

    @Override
    public List<User> findAll() {

        List<User> users = new ArrayList<>();

        List<Integer> usersId = jdbcTemplate.query("SELECT user_id FROM USERS", ((rs, rowNum) -> rs.getInt("user_id")));

        for (Integer userId : usersId) {
            users.add(getUserById(userId));
        }

        return users;
    }

    @Override
    public User getUserById(Integer id) {

        User user = usersDao.getUserById(id);

        user.setFriends(friendsListDao.getFriendListById(id));
        user.setSubscribers(subscribersListDao.getSubscribesListById(id));

        return user;

    }

    @Override
    public boolean isContains(Integer id) {
        String sql = "SELECT * FROM USERS WHERE user_id = ?";
        return jdbcTemplate.queryForRowSet(sql, id).next();
    }
}
