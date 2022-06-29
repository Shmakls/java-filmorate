package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

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

        return filmService.topLikes(count);

    }

    @DeleteMapping("/{filmId}")
    public void deleteFilm(@PathVariable int filmId) {

        filmService.deleteFilm(filmId);

    }
}
