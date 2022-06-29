package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Review {
    private int id;
    @NotNull private int userId;
    @NotNull  private int filmId;
    @NotBlank private String content;
    private int useful;
    @NotNull private boolean isPositive;
}