package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Genre {

    private int id;
    private String name;

    public Genre(int id) {
        this.id = id;
    }

}
