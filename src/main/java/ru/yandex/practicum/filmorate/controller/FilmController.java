package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private Map<Integer, Film> films = new HashMap<Integer, Film>();

    private FilmValidator filmValidator = new FilmValidator();

    @PostMapping
    public void addFilm(@RequestBody Film film) {

        filmValidator.validator(film);

        films.put(film.getId(), film);

        log.info("Получен запрос на добавление фильма - " + film.getName());

    }

    @PutMapping
    public void updateFilm(@RequestBody Film film) {

        filmValidator.validator(film);

        films.put(film.getId(), film);

        log.info("Получен запрос на обновление фильма - " + film.getName());

    }

    @GetMapping
    public List<Film> findAll() {

        return new ArrayList<>(films.values());

    }


}
