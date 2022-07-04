package ru.yandex.practicum.filmorate.storage.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Component
public class GenresListDao {

    private final JdbcTemplate jdbcTemplate;

    public GenresListDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveGenresListByFilm(Film film) {

        String sql = "INSERT INTO GENRESLIST (film_id, genre_id) VALUES (" + film.getId() + ", ?)";

        List<Genre> genres = film.getGenres();

        if (genres != null) {

            Set<Genre> buffer = new HashSet<>(genres);
            genres = new ArrayList<>(buffer);

            genres.sort(Comparator.comparingInt(Genre::getId));

            film.setGenres(genres);

            for (Genre genre : genres) {
                jdbcTemplate.update(sql, genre.getId());
            }
        }

    }

    public Set<Integer> getGenresSetIdByFilmId(Integer id) {

        String sql = "SELECT genre_id FROM GENRESLIST WHERE film_id = ?";

        List<Integer> genres = jdbcTemplate.query(sql, ((rs, rowNum) -> rs.getInt("genre_id")), id);

        return new HashSet<>(genres);

    }

    public void delete(Integer id) {

        String sql = "DELETE FROM GENRESLIST WHERE film_id = ?";

        jdbcTemplate.update(sql, id);

    }


}
