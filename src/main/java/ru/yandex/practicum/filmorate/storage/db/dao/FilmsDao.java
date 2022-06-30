package ru.yandex.practicum.filmorate.storage.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.*;

@Component
public class FilmsDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Film save(Film film) {

        String insertSql = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASEDATE, DURATION, RATING_ID) VALUES (?, ?, ?, ?, ?)";

        String selectSql = "SELECT film_id FROM FILMS WHERE NAME = ? AND DESCRIPTION = ?";



        jdbcTemplate.update(insertSql, film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());

        SqlRowSet rs = jdbcTemplate.queryForRowSet(selectSql, film.getName(), film.getDescription());

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

    public List<Film> getCommonFilms(Integer userId, Integer friendId) {

        String sql = "SELECT f.film_id, f.name, description, releasedate, duration, f.rating_id FROM" +
                " films AS f " +
                "LEFT JOIN (SELECT film_id, COUNT(film_id) AS count_like FROM likeslist GROUP BY film_id) USING (film_id) " +
                "RIGHT JOIN likeslist AS l1 ON f.film_id = l1.film_id " +
                "RIGHT JOIN likeslist AS l2 ON l1.film_id = l2.film_id " +
                "WHERE l1.user_id = ? AND l2.user_id = ?" +
                "ORDER BY count_like DESC;";

        String mpa_SQL = "SELECT * FROM MPA;";

        List<Film> result = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql,userId,friendId);

        if (rowSet.next()) {
           Film film = new Film(rowSet.getString("name"),
                    rowSet.getString("description"),
                    rowSet.getDate("releaseDate").toLocalDate(),
                    rowSet.getInt("duration"));
           film.setId(rowSet.getInt("film_id"));
           film.setMpa(new MpaDao(jdbcTemplate).getMpaById(rowSet.getInt("rating_id")));
           result.add(film);
        }

        return result;
    }

}
