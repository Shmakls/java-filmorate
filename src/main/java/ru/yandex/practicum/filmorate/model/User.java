package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@RequiredArgsConstructor
public class User {

    private Integer id;

    private final String email;

    private final String login;

    private String name;

    private final LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();

}
