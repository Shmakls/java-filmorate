package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.util.Date;
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
                                      @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int date) {

        return filmService.topLikes(count, genreId, date);
    }
}
