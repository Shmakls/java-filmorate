package ru.yandex.practicum.filmorate.storage.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Set;

@Component
public class SubscribesListDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SubscribesListDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Set<Integer> getSubscribesListById(Integer id) {

        String sql = "SELECT subscribe_id FROM SUBSCRIBESLIST WHERE user_id = ?";

        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("subscribe_id"), id));

    }

    public void delete(Integer id) {

        String sql = "DELETE FROM SUBSCRIBESLIST WHERE user_id = ?";

        jdbcTemplate.update(sql, id);

    }

    public void saveSubscribesList(User user) {

        String sql = "INSERT INTO SUBSCRIBESLIST (user_id, subscribe_id) VALUES (" + user.getId() + ", ?)";

        Set<Integer> subscribersId = user.getSubscribers();

        for (Integer subscriberId : subscribersId) {
            jdbcTemplate.update(sql, subscriberId);
        }

    }
}
