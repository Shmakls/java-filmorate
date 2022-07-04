package ru.yandex.practicum.filmorate.storage.FilmStorage;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmStorage {

    Film save(Film film);

    Film update(Film film);

    void delete(Integer id);

    List<Film> findAll();

    Film getFilmById (Integer id);

    boolean contains(Integer id);

    Genre getGenreById(Integer id);

    List<Genre> getAllGenres();

    Mpa getMpaById(Integer id);

    List<Mpa> getAllMpa();

    List<Integer> getTopFilms(int count);

    List<Integer> getTopYearFilm(int year, int limit);

    List<Integer> getTopGenreFilm(int genreId);

    List<Film> getCommonFilms(Integer user1, Integer user2);

    List<Integer> getFilmsByDirectorSorted(int directorId, String sortBy);

    List<Director> getAllDirectors();

    Director createDirector(Director director);

    Director updateDirector(Director director);

    void removeDirector(Integer director_id);

    Director getDirectorById(Integer id);

    boolean isExists(Integer id);

}
