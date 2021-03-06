package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode
public class Film {

    private Integer id;

    private final String name;

    private final String description;

    private final LocalDate releaseDate;

    private final int duration;

    private Set<Integer> likes = new HashSet<>();

    private List<Genre> genres;

    private Mpa mpa;

    private List<Director> directors;
}
