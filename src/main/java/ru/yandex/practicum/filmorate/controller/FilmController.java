package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.Context;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private FilmService filmService = Context.FILM_SERVICE;

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {

        log.info("Получен запрос на добавление фильма - {}", film.getName());

        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {

        log.info("Получен запрос на обновление фильма - {}", film.getName());

        return filmService.updateFilm(film);

    }

    @GetMapping
    public List<Film> findAll() {

        return filmService.findAll();

    }

    @PutMapping("/{id}/like/{userId}")
    public String userGiveLikeToFilm(@PathVariable int id, @PathVariable int userId) {

        return filmService.addLike(id, userId);

    }

    @DeleteMapping("/{id}/like/{userId}")
    public String userDeleteLikeToFilm(@PathVariable int id, @PathVariable int userId) {

        return filmService.deleteLike(id, userId);

    }

    @GetMapping("/popular")
    public List<Film> topFilmsByLikes(@RequestParam(defaultValue = "10") Integer count) {

        if (count < 1) {
            throw new IncorrectParameterException("Количество отображаемых фильмов не может быть меньше 1", count);
        }

        return filmService.topLikes(count);

    }


}
