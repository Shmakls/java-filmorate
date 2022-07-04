package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Mpa {

    private int id;
    private String name;

    public Mpa(int id) {
        this.id = id;
    }

}
