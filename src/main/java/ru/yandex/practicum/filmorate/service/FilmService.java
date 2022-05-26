package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage.FilmStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private FilmStorage filmStorage;
    private UserService userService;

    private FilmValidator filmValidator;

    private Integer id = 0;

    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.userService = userService;
        this.filmStorage = filmStorage;
        filmValidator = new FilmValidator();
    }

    public Film addFilm(Film film) {

        filmValidator.isValid(film);

        film.setId(++id);

        return filmStorage.add(film);

    }

    public Film updateFilm(Film film) {

        filmValidator.isValid(film);

        if (film.getId() < 1) {
            throw new IncorrectIdException("ИДИ НАХУЙ БЛЯТЬ С ТАКИМ ID");
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

        return filmStorage.getById(id);

    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public String addLike(Integer id, Integer userId) {

        if (!filmStorage.isContains(id)) {
            throw new IncorrectIdException("Такого фильма не существует");
        }

        Film film = filmStorage.getById(id);

        if (!film.getLikes().add(userId)) {
            throw new AlreadyExistException("Пользователь уже лайкнул этот фильм");
        }

        return ("Пользователь " + userService.getUserById(userId).getLogin() + " поставил лайк фильму "
                + filmStorage.getById(id).getName() + ".");

    }

    public String deleteLike(Integer id, Integer userId) {

        if (!filmStorage.isContains(id)) {
            throw new IncorrectIdException("Такого фильма не существует");
        }

        Film film = filmStorage.getById(id);

        if (!film.getLikes().contains(userId)) {
            throw new IncorrectIdException("Этот пользователь не ставил лайк этому фильму");
        }

        film.getLikes().remove(userId);

        return ("Пользователь " + userService.getUserById(userId).getLogin() + " убрал лайк у фильма "
                + filmStorage.getById(id).getName() + ".");

    }

    public List<Film> topLikes(Integer count) {

        if (count > filmStorage.findAll().size()) {
            count = filmStorage.findAll().size();
        }

        return new ArrayList<>(filmStorage.findAll()).stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());

    }

}
