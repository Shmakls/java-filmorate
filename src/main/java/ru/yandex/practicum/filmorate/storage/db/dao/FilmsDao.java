package ru.yandex.practicum.filmorate.storage.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

@Component
public class FilmsDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Film save(Film film) {

        String insertSql = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASEDATE, DURATION, RATING_ID) VALUES (?, ?, ?, ?, ?)";

        String selectSql = "SELECT film_id FROM FILMS WHERE NAME = ?";



        jdbcTemplate.update(insertSql, film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());

        SqlRowSet rs = jdbcTemplate.queryForRowSet(selectSql, film.getName());

        int id = 0;

        if (rs.next()) {
            id = rs.getInt("film_id");
        }

        film.setId(id);

        return film;

    }

    public Film getFilmById(Integer id) {

        String sql = "SELECT * FROM FILMS WHERE film_id = ?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);

        if (rowSet.next()) {

            Film film = new Film(rowSet.getString("name"),
                    rowSet.getString("description"),
                    rowSet.getDate("releaseDate").toLocalDate(),
                    rowSet.getInt("duration"));

            film.setId(rowSet.getInt("film_id"));
            film.setMpa(new Mpa(rowSet.getInt("rating_id")));

            return film;
        } else {
            throw new IncorrectIdException("Отсутсвуют данные в БД по указанному ID");
        }

    }

    public void deleteFilm(Integer id) {

        String sql = "DELETE FROM FILMS WHERE film_id = ?";

        jdbcTemplate.update(sql, id);

    }

}
