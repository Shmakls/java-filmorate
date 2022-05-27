package ru.yandex.practicum.filmorate.storage.FilmStorage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Integer, Film> films = new ConcurrentHashMap<>();

    @Override
    public Film add(Film film) {

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
    public Film getById(Integer id) {
        return films.get(id);
    }

    @Override
    public boolean isContains(Integer id) {
        return films.containsKey(id);
    }
}
