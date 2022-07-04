package ru.yandex.practicum.filmorate.storage.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class DirectorDao {

    private final JdbcTemplate jdbcTemplate;

    public DirectorDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Director getDirectorById(Integer id) {

        return getAllDirectors()
                .stream()
                .filter(x -> x.getId() == id).findFirst().
                orElseThrow(() -> new IncorrectIdException("По данному id " + id + " режиссера не найдено"));
    }

    public List<Director> getAllDirectors() {
        String sql = "SELECT * FROM directors";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeDirector(rs)));
    }

    public Director createDirector(Director director) {
        String sql = "INSERT INTO directors (name) " +
                "VALUES (?)";

        jdbcTemplate.update(sql, director.getName());

        String sqlReturn = "SELECT * FROM directors WHERE name = ? ;";

        return jdbcTemplate
                .query(sqlReturn, ((rs, rowNum) -> makeDirector(rs)), director.getName())
                .stream()
                .findFirst()
                .orElseThrow();
    }

    public Director updateDirector(Director director) {
        String sql = "UPDATE directors" +
                " SET name = ?" +
                " WHERE director_id = ?";

        jdbcTemplate.update(sql, director.getName(), director.getId());

        String sqlReturn = "SELECT * FROM directors WHERE name = ? ;";

        return jdbcTemplate
                .query(sqlReturn, ((rs, rowNum) -> makeDirector(rs)), director.getName())
                .stream()
                .findFirst()
                .orElseThrow();
    }

    public void removeDirector(int id) {
        String sql = "DELETE FROM directors WHERE director_id = ?";

        jdbcTemplate.update(sql, id);
    }

    private Director makeDirector(ResultSet rs) throws SQLException {
        int id = rs.getInt("director_id");
        String name = rs.getString("name");

        return new Director(id, name);
    }
}
