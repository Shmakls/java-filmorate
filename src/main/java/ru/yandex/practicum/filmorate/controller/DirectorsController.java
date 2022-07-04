package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorsController {

    private final FilmService filmService;

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable @Positive int id) {
        return filmService.getDirectorById(id);
    }

    @GetMapping()
    public List<Director> getAllDirectors() {
        return filmService.findAllDirectors();
    }

    @PostMapping()
    public Director createDirector(@RequestBody Director director) {
        return filmService.createDirector(director);
    }

    @PutMapping()
    public Director updateDirector(@RequestBody Director director) {
        return filmService.updateDirector(director);
    }

    @DeleteMapping("/{id}")
    public void removeDirector(@PathVariable int id) {
        filmService.removeDirector(id);
    }
}
