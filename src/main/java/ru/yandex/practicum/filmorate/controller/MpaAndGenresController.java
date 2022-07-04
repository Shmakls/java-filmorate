package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MpaAndGenresController {

    private final FilmService filmService;

    @GetMapping("/mpa/{id}")
    public Mpa getMpaById(@PathVariable int id) {

        return filmService.getMpaById(id);

    }

    @GetMapping("/mpa")
    public List<Mpa> getAllMpa() {
        return filmService.findAllMpa();
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        return filmService.findAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable int id) {
        return filmService.getGenreById(id);
    }

}
