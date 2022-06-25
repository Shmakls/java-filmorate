package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mpa {

    private int id;
    private String name;

    public Mpa() {
    }

    public Mpa(int id) {
        this.id = id;
    }

    public Mpa(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mpa mpa = (Mpa) o;
        return id == mpa.id;
    }

}
