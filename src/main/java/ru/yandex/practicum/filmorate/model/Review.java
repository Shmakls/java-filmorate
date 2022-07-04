package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class Review {
    private Integer id;
    @NotBlank private String content;
    @JsonProperty("isPositive") private Boolean isPositive;
    @NotNull private Integer userId;
    @NotNull private Integer filmId;
    @NotNull private Integer useful;
}