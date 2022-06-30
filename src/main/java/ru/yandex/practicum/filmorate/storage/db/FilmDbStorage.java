package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.db.dao.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private JdbcTemplate jdbcTemplate;
    private FilmsDao filmsDao;
    private LikesListDao likesListDao;
    private GenresListDao genresListDao;
    private MpaDao mpaDao;
    private GenresDao genresDao;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmsDao filmsDao, LikesListDao likesListDao, GenresListDao genresListDao, MpaDao mpaDao, GenresDao genresDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmsDao = filmsDao;
        this.likesListDao = likesListDao;
        this.genresListDao = genresListDao;
        this.mpaDao = mpaDao;
        this.genresDao = genresDao;
    }

    @Override
    public Film save(Film film) {

        Film filmWithId = filmsDao.save(film);

        filmWithId.setMpa(mpaDao.getMpaById(filmWithId.getMpa().getId()));

        genresListDao.saveGenresListByFilm(filmWithId);
        likesListDao.saveLikesListByFilm(filmWithId);

        return filmWithId;

    }

    @Override
    public Film update(Film film) {

        if (isContains(film.getId())) {

            String sql = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, rating_id = ? WHERE film_id = ?";

            likesListDao.delete(film.getId());
            likesListDao.saveLikesListByFilm(film);

            genresListDao.delete(film.getId());
            genresListDao.saveGenresListByFilm(film);

            jdbcTemplate.update(sql, film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());

        } else {
            save(film);
        }

        return film;

    }

    @Override
    public void delete(Integer id) {

        genresListDao.delete(id);

        likesListDao.delete(id);

        filmsDao.deleteFilm(id);

    }

    @Override
    public List<Film> findAll() {

        List<Film> films = new ArrayList<>();

        List<Integer> filmsId = jdbcTemplate.query("SELECT film_id FROM FILMS", ((rs, rowNum) -> rs.getInt("film_id")));

        for (Integer filmId : filmsId) {
            films.add(getFilmById(filmId));
        }

        return films;

    }

    @Override
    public Film getFilmById(Integer id) {

        Film film = filmsDao.getFilmById(id);

        film.setLikes(likesListDao.getLikesListById(id));

        List<Genre> genres = null;
        Set<Integer> genresId = genresListDao.getGenresSetIdByFilmId(id);

        if (genresId.size() > 0) {
            genres = new ArrayList<>();
            for (Integer genreId : genresId) {
                genres.add(genresDao.getGenreById(genreId));
            }
        }

        film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));

        film.setGenres(genres);

        return film;
    }

    @Override
    public boolean isContains(Integer id) {
        String sql = "SELECT * FROM FILMS WHERE film_id = ?";
        return jdbcTemplate.queryForRowSet(sql, id).next();
    }

    @Override
    public Genre getGenreById(Integer id) {
        return genresDao.getGenreById(id);
    }

    @Override
    public List<Genre> getAllGenres() {
        return genresDao.getAllGenres();
    }

    @Override
    public Mpa getMpaById(Integer id) {
        return mpaDao.getMpaById(id);
    }

    @Override
    public List<Mpa> getAllMpa() {
        return mpaDao.getAllMpa();
    }
}
