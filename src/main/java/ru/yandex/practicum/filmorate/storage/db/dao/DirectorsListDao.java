package ru.yandex.practicum.filmorate.storage.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DirectorsListDao {

    private final JdbcTemplate jdbcTemplate;

    public DirectorsListDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveDirectorsListByFilm(Film film) {
        String sql = "INSERT INTO directors_list (film_id, director_id)" +
                " VALUES (?, ?)";
        List<Director> directors = film.getDirectors();

        if (directors != null) {
            Set<Director> buffer = new HashSet<>(directors);
            directors = new ArrayList<>(buffer);
            film.setDirectors(directors);

            for (Director director : directors) {
                jdbcTemplate.update(sql, film.getId(), director.getId());
            }
        }
    }

    public Set<Integer> getDirectorsSetIdByFilmId(Integer id) {
        String sql = "SELECT director_id FROM directors_list WHERE film_id = ?";
        List<Integer> directors = jdbcTemplate
                .query(sql, ((rs, rowNum) -> rs.getInt("director_id")), id);

        return new HashSet<>(directors);
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM directors_list WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Integer> getFilmsByDirectorSortedByLikes(Integer id) {
        String sql =
                "SELECT films.film_id " +
                        "FROM films " +
                        "LEFT JOIN LikesList AS likes ON films.film_id = likes.film_id " +
                        "LEFT JOIN directors_list ON films.film_id = directors_list.film_id " +
                        "LEFT JOIN directors ON directors_list.director_id = directors.director_id " +
                        "WHERE directors.director_id = ? " +
                        "GROUP BY films.film_id " +
                        "ORDER BY COUNT (likes.user_id) DESC;";

        return jdbcTemplate
                .query(sql, ((rs, rowNum) -> rs.getInt("film_id")), id);
    }

    public List<Integer> getFilmsByDirectorSortedByYear(Integer id) {
        String sql =
                "SELECT films.film_id " +
                        "FROM films " +
                        "LEFT JOIN directors_list ON films.film_id = directors_list.film_id " +
                        "LEFT JOIN directors ON directors_list.director_id = directors.director_id " +
                        "WHERE directors.director_id = ? " +
                        "ORDER BY films.releaseDate ASC;";

        return jdbcTemplate
                .query(sql, ((rs, rowNum) -> rs.getInt("film_id")), id);
    }

    public List<Integer> getFilmsByDirectorNotSorted(Integer id) {
        String sql =
                "SELECT films.film_id " +
                        "FROM films " +
                        "LEFT JOIN LikesList AS likes ON films.film_id = likes.film_id " +
                        "LEFT JOIN directors_list ON films.film_id = directors_list.film_id " +
                        "LEFT JOIN directors ON directors_list.director_id = directors.director_id " +
                        "WHERE directors.director_id = ? " +
                        "GROUP BY directors_list.director_id;";

        return jdbcTemplate
                .query(sql, ((rs, rowNum) -> rs.getInt("film_id")), id);
    }
}