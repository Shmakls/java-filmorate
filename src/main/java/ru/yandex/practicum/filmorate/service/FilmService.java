package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage.FilmStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private FilmStorage filmStorage;
    private UserService userService;

    private FilmValidator filmValidator;

    private Integer id = 0;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, UserService userService) {
        this.userService = userService;
        this.filmStorage = filmStorage;
        filmValidator = new FilmValidator();
    }

    public Film addFilm(Film film) {

        filmValidator.isValid(film);

        film.setId(++id);

        return filmStorage.save(film);

    }

    public Film updateFilm(Film film) {

        filmValidator.isValid(film);

        if (film.getId() < 1) {
            throw new IncorrectIdException("ID не может быть отрицательным");
        }

        return filmStorage.update(film);

    }

    public void deleteFilm(Integer id) {

        if (!filmStorage.isContains(id)) {
            throw new IncorrectIdException("Такой ID не сушествует.");
        }

        filmStorage.delete(id);
    }

    public Film getFilmById(Integer id) {

        if (!filmStorage.isContains(id)) {
            throw new IncorrectIdException("Такой ID не сушествует.");
        }

        return filmStorage.getFilmById(id);

    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public String addLike(Integer id, Integer userId) {

        if (!filmStorage.isContains(id)) {
            throw new IncorrectIdException("Такого фильма не существует");
        }

        Film film = filmStorage.getFilmById(id);

        Set<Integer> likes = film.getLikes();

        if (!likes.add(userId)) {
            throw new AlreadyExistException("Пользователь уже лайкнул этот фильм");
        }

        filmStorage.update(film);

        return ("Пользователь " + userService.getUserById(userId).getLogin() + " поставил лайк фильму "
                + filmStorage.getFilmById(id).getName() + ".");

    }

    public String deleteLike(Integer id, Integer userId) {

        if (!filmStorage.isContains(id)) {
            throw new IncorrectIdException("Такого фильма не существует");
        }

        Film film = filmStorage.getFilmById(id);

        if (!film.getLikes().contains(userId)) {
            throw new IncorrectIdException("Этот пользователь не ставил лайк этому фильму");
        }

        film.getLikes().remove(userId);

        updateFilm(film);

        return ("Пользователь " + userService.getUserById(userId).getLogin() + " убрал лайк у фильма "
                + filmStorage.getFilmById(id).getName() + ".");

    }

    public List<Film> topLikes(Integer count) {

        if (count < 1) {
            throw new IncorrectParameterException("Количество отображаемых фильмов не может быть меньше 1", count);
        }

        if (count > filmStorage.findAll().size()) {
            count = filmStorage.findAll().size();
        }

        return new ArrayList<>(filmStorage.findAll()).stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());

    }

    public List<Genre> findAllGenres() {
        return filmStorage.getAllGenres();
    }

    public Genre getGenreById(Integer id) {

        List<Genre> allGenres = findAllGenres();

        if (id > allGenres.size() || id < 1) {
            throw new IncorrectIdException("Некорректиный Id жанра");
        }

        return allGenres.stream().filter(x -> x.getId() == id).findFirst().get();

    }

    public Mpa getMpaById(Integer id) {

        List<Mpa> allMpa = findAllMpa();

        if (id > allMpa.size() || id < 1) {
            throw new IncorrectIdException("Некорректный Id рейтинга");
        }

        return allMpa.stream().filter(x -> x.getId() == id).findFirst().get();

    }

    public List<Mpa> findAllMpa() {
        return filmStorage.getAllMpa();
    }

}
