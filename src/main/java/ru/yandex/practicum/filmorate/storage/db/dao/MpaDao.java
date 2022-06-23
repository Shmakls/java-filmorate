package ru.yandex.practicum.filmorate.storage.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDao {

    private JdbcTemplate jdbcTemplate;

    public MpaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa getMpaById(Integer id) {

        List<Mpa> allMpa = getAllMpa();

        return allMpa.stream().filter(x -> x.getId() == id).findFirst().get();



    }

    public List<Mpa> getAllMpa() {

        String sql = "SELECT * FROM MPA";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeMpa(rs)));

    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        int id = rs.getInt("rating_id");
        String name = rs.getString("name");

        return new Mpa(id, name);
    }

}
