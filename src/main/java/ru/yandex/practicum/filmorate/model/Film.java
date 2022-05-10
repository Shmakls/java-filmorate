package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

@Data
public class Film {

    private final int id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;

}
