package ru.yandex.practicum.filmorate.storage.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenresDao {

    private JdbcTemplate jdbcTemplate;

    public GenresDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre getGenreById(Integer id) {

        List<Genre> allGenres = getAllGenres();

        return allGenres.stream().filter(x -> x.getId() == id).findFirst().get();

    }

    public List<Genre> getAllGenres() {

        String sql = "SELECT * FROM GENRES";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeGenre(rs)));

    }

    private Genre makeGenre(ResultSet rs) throws SQLException {

        int id = rs.getInt("genre_id");
        String name = rs.getString("name");

        return new Genre(id, name);

    }

}
