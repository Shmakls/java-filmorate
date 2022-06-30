package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
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
    public List<Film> topFilmsByLikes(@RequestParam(required = false, defaultValue = "10") @Positive int count,
                                      @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int genreId,
                                      @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int year) {
        log.info("Получен запрос на получение топ фильмов count = {}, genreId = {}, date = {}", count, genreId, year);
        return filmService.topLikes(count, genreId, year);
    }
}
