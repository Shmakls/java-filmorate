package ru.yandex.practicum.filmorate.storage.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Set;

@Component
public class FriendsListDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendsListDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Set<Integer> getFriendListById(Integer id) {

        String sql = "SELECT friend_id FROM FRIENDLIST WHERE user_id = ?";

        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("friend_id"), id));

    }

    public void delete(Integer id) {

        String sql = "DELETE FROM FRIENDLIST WHERE user_id = ?";

        jdbcTemplate.update(sql, id);

    }

    public void saveFriendList(User user) {

        String sql = "INSERT INTO FRIENDLIST (user_id, friend_id) VALUES (" + user.getId() + ", ?)";

        Set<Integer> friendsId = user.getFriends();

        for (Integer friendId : friendsId) {
            jdbcTemplate.update(sql, friendId);
        }

    }
}
