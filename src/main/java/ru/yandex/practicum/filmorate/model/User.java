package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.Date;

@Data
public class User {

    private final int id;
    @Email
    private final String email;
    private final String login;
    private String name;
    private LocalDate birthday;

}
