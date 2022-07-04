package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Data
@RequiredArgsConstructor
@EqualsAndHashCode
public class User {

    private Integer id;

    private final String email;

    private final String login;

    private String name;

    private final LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();

    private Set<Integer> filmsLikes = new HashSet<>();

}
