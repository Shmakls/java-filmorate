package ru.yandex.practicum.filmorate.storage.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UsersDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UsersDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User getUserById(Integer id) {

        String sql = "SELECT * FROM USERS WHERE user_id = ?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);

        if(rowSet.next()) {

            User user = new User(rowSet.getString("email"),
                    rowSet.getString("login"),
                    rowSet.getDate("birthday").toLocalDate());

            user.setName(rowSet.getString("name"));
            user.setId(id);

            return user;
        } else {
            throw new IncorrectIdException("Отсутствуют данные в БД по указанному ID");
        }
    }

    public void deleteUserById(Integer id) {

        String sql = "DELETE FROM USERS WHERE user_id = ?";

        jdbcTemplate.update(sql, id);

    }

    public User save(User user) {

        String insertSql = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES (?, ?, ?, ?)";

        String selectSql = "SELECT USER_ID FROM USERS WHERE EMAIL = ?";

        jdbcTemplate.update(insertSql, user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        SqlRowSet rs = jdbcTemplate.queryForRowSet(selectSql, user.getEmail());

        int id = 0;

        if (rs.next()) {
            id = rs.getInt("user_id");
        }
        user.setId(id);

        return user;

    }
}
