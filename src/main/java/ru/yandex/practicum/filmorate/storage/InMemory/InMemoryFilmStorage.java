package ru.yandex.practicum.filmorate.storage.InMemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Qualifier("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Integer, Film> films = new ConcurrentHashMap<>();

    @Override
    public Film save(Film film) {

        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film update(Film film) {

        films.put(film.getId(), film);

        return film;
    }

    @Override
    public void delete(Integer id) {
        films.remove(id);
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Integer id) {
        return films.get(id);
    }

    @Override
    public boolean isContains(Integer id) {
        return films.containsKey(id);
    }

    @Override
    public Genre getGenreById(Integer id) {
        return null;
    }

    @Override
    public List<Genre> getAllGenres() {
        return null;
    }

    @Override
    public Mpa getMpaById(Integer id) {
        return null;
    }

    @Override
    public List<Mpa> getAllMpa() {
        return null;
    }

    @Override
    public List<Integer> getTopFilms(int count) {
        return null;
    }

    @Override
    public List<Integer> getTopYearFilm(int year) {
        return null;
    }

    @Override
    public List<Integer> getTopGenreFilm(int genreId) {
        return null;
    }
}
