package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.db.dao.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmsDao filmsDao;
    private final LikesListDao likesListDao;
    private final GenresListDao genresListDao;
    private final MpaDao mpaDao;
    private final GenresDao genresDao;

    private final DirectorDao directorDao;

    private final DirectorsListDao directorsListDao;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         FilmsDao filmsDao,
                         LikesListDao likesListDao,
                         GenresListDao genresListDao,
                         MpaDao mpaDao,
                         GenresDao genresDao,
                         DirectorDao directorDao,
                         DirectorsListDao directorsListDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmsDao = filmsDao;
        this.likesListDao = likesListDao;
        this.genresListDao = genresListDao;
        this.mpaDao = mpaDao;
        this.genresDao = genresDao;
        this.directorsListDao = directorsListDao;
        this.directorDao = directorDao;
    }

    @Override
    public Film save(Film film) {

        Film filmWithId = filmsDao.save(film);

        filmWithId.setMpa(mpaDao.getMpaById(filmWithId.getMpa().getId()));

        genresListDao.saveGenresListByFilm(filmWithId);
        likesListDao.saveLikesListByFilm(filmWithId);
        directorsListDao.saveDirectorsListByFilm(filmWithId);

        return filmWithId;

    }

    @Override
    public Film update(Film film) {

        if (contains(film.getId())) {

            String sql = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, rating_id = ? WHERE film_id = ?";

            likesListDao.delete(film.getId());
            likesListDao.saveLikesListByFilm(film);

            genresListDao.delete(film.getId());
            genresListDao.saveGenresListByFilm(film);

            directorsListDao.delete(film.getId());
            directorsListDao.saveDirectorsListByFilm(film);

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

        List<Integer> filmsId = jdbcTemplate.query("SELECT film_id FROM FILMS ORDER BY film_id ASC;", ((rs, rowNum) -> rs.getInt("film_id")));

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

        List<Director> directors = new ArrayList<>();
        //List<Director> directors = null;
        Set<Integer> directorsId = directorsListDao.getDirectorsSetIdByFilmId(id);

        if (directorsId.size() > 0) {
            directors = new ArrayList<>();
            for (Integer directorId : directorsId) {
                directors.add(directorDao.getDirectorById(directorId));
            }
        }

        film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));

        film.setGenres(genres);

        film.setDirectors(directors);

        return film;
    }

    @Override
    public boolean contains(Integer id) {
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

    @Override
    public List<Integer> getTopFilms(int count) {
        return likesListDao.getTopFilms(count);
    }

    @Override
    public List<Integer> getTopYearFilm(int year) {
        return likesListDao.getTopFilmsByYear(year);
    }

    @Override
    public List<Integer> getTopGenreFilm(int genreId) {
        return likesListDao.getTopFilmsByGenre(genreId);
    }

    @Override
    public List<Film> getCommonFilms(Integer user1, Integer user2) {
        return filmsDao.getCommonFilms(user1,user2);
    }

    @Override
    public List<Integer> getFilmsByDirectorSorted(int directorId, String sortBy) {
        switch (sortBy) {
            case "year":
                return directorsListDao.getFilmsByDirectorSortedByYear(directorId);
            case "likes":
                return directorsListDao.getFilmsByDirectorSortedByLikes(directorId);
            default:
                return directorsListDao.getFilmsByDirectorNotSorted(directorId);
        }
    }

    @Override
    public List<Director> getAllDirectors() {
        return directorDao.getAllDirectors();
    }

    @Override
    public Director createDirector(Director director) {
        return directorDao.createDirector(director);
    }

    @Override
    public Director updateDirector(Director director) {
        return directorDao.updateDirector(director);
    }

    @Override
    public void removeDirector(Integer id) {
        directorDao.removeDirector(id);
    }

    @Override
    public Director getDirectorById(Integer id) {
        return directorDao.getDirectorById(id);
    }

    @Override
    public boolean isExists(Integer id) {
        return !(getFilmById(id) == null);
    }
}
