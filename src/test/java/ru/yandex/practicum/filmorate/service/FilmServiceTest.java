package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemory.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemory.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmServiceTest {

    private final FilmService filmService;
    private final UserService userService;

    Film film1;
    Film film2;

    User user1;

    @BeforeEach
    void beforeEach() {

        user1 = new User("user1@ya.ru", "user1", LocalDate.of(1989, 7, 29));
        user1.setName("Пользователь 1");
        userService.createUser(user1);

        createFilms();

        filmService.addFilm(film1);
        filmService.addFilm(film2);

    }

    private void createFilms() {

        film1 = new Film("film1", "descriptionFilm1", LocalDate.of(2020, 12, 12), 90);
        film1.setId(1);
        film1.setMpa(new Mpa(1));
        film2 = new Film("film2", "descriptionFilm2", LocalDate.of(2020, 11, 11), 100);
        film2.setId(2);
        film2.setMpa(new Mpa(2));

    }

    @Test
    void shouldBeAddLikeToFilm() {

        filmService.addLike(1, 1);

        assertTrue(filmService.getFilmById(1).getLikes().contains(1));
    }

    @Test
    void shouldBeThrowExceptionIfFilmIdIsNotExistByAddLike() {

        final IncorrectIdException exception = assertThrows(
                IncorrectIdException.class,
                () -> filmService.addLike(3, 1)
        );
    }

    @Test
    void shouldBeThrowExceptionIfAlreadyLike() {

        filmService.addLike(1, 1);

        final AlreadyExistException exception = assertThrows(
                AlreadyExistException.class,
                () -> filmService.addLike(1, 1)
        );

    }

    @Test
    void shouldBeAddFilmToRepository() {

        Film film3 = new Film("film3", "descriptionFilm3", LocalDate.of(2020, 10, 10), 110);
        film3.setId(3);
        film3.setMpa(new Mpa(1));

        filmService.addFilm(film3);

        assertEquals(3, filmService.findAll().size());

    }

    @Test
    void shouldBeAddFilmWhenIsUpdate() {

        Film film3 = new Film("film3", "descriptionFilm3", LocalDate.of(2020, 10, 10), 110);
        film3.setId(3);
        film3.setMpa(new Mpa(1));

        filmService.updateFilm(film3);

        Film film3FromRep = filmService.getFilmById(3);

        assertEquals(3, film3FromRep.getId());

    }

    @Test
    void shouldBeDeleteFilmFromRep() {

        filmService.deleteFilm(1);

        assertEquals(1, filmService.findAll().size());

    }

    @Test
    void shouldBeThrowExceptionIfDeleteIdNotExist() {

        final IncorrectIdException exception = assertThrows(
                IncorrectIdException.class,
                () -> filmService.deleteFilm(3)
        );

    }

    @Test
    void shouldBeGetFilmById() {

        Film testFilm = filmService.getFilmById(1);

        assertEquals(film1, testFilm);

    }

    @Test
    void shouldBeThrowExceptionIfIdIsNotExist() {

        final IncorrectIdException exception = assertThrows(
                IncorrectIdException.class,
                () -> filmService.getFilmById(3)
        );

    }

    @Test
    void shouldBeReturnListOfAllFilms() {

        List<Film> testList = filmService.findAll();

        assertEquals(2, testList.size());

    }

    @Test
    void shouldBeDeleteLikeFromFilm() {

        filmService.addLike(1, 1);

        assertEquals(1, filmService.getFilmById(1).getLikes().size());

        filmService.deleteLike(1, 1);

        assertEquals(0, filmService.getFilmById(1).getLikes().size());

    }

    @Test
    void shouldBeThrowExceptionIfFilmIdIsNotExistByDeleteLike() {

        filmService.addLike(1, 1);

        final IncorrectIdException exception = assertThrows(
                IncorrectIdException.class,
                () -> filmService.deleteLike(3, 1)
        );

    }

    @Test
    void shouldBeThrowExceptionIfUserIdIsNotExistByDeleteLike() {

        filmService.addLike(1, 1);

        final IncorrectIdException exception = assertThrows(
                IncorrectIdException.class,
                () -> filmService.deleteLike(1, 2)
        );

    }

    @Test
    void shouldBeReturnFilmRating() {

        User user2 = new User("user2@ya.ru", "user2", LocalDate.of(1989, 7, 29));
        user2.setName("Пользователь 2");
        userService.createUser(user2);

        User user3 = new User("user3@ya.ru", "user3", LocalDate.of(1989, 7, 29));
        user3.setName("Пользователь 3");
        userService.createUser(user3);

        User user4 = new User("user4@ya.ru", "user4", LocalDate.of(1989, 7, 29));
        user4.setName("Пользователь 4");
        userService.createUser(user4);

        User user5 = new User("user5@ya.ru", "user5", LocalDate.of(1989, 7, 29));
        user5.setName("Пользователь 5");
        userService.createUser(user5);

        Film film3 = new Film("film3", "descriptionFilm3", LocalDate.of(2020, 10, 10), 110);
        film3.setId(3);
        film3.setMpa(new Mpa(1));
        Film film4 = new Film("film4", "descriptionFilm4", LocalDate.of(2020, 9, 9), 120);
        film4.setId(4);
        film4.setMpa(new Mpa(1));
        Film film5 = new Film("film5", "descriptionFilm5", LocalDate.of(2020, 8, 8), 130);
        film5.setId(5);
        film5.setMpa(new Mpa(1));

        filmService.addFilm(film3);
        filmService.addFilm(film4);
        filmService.addFilm(film5);

        filmService.addLike(1, 1);

        filmService.addLike(2, 1);
        filmService.addLike(2, 2);

        filmService.addLike(3, 1);
        filmService.addLike(3, 2);
        filmService.addLike(3, 3);

        filmService.addLike(4, 1);
        filmService.addLike(4, 2);
        filmService.addLike(4, 3);
        filmService.addLike(4, 4);

        filmService.addLike(5, 1);
        filmService.addLike(5, 2);
        filmService.addLike(5, 3);
        filmService.addLike(5, 4);
        filmService.addLike(5, 5);

        assertEquals(5, filmService.findAll().size());

        List<Film> testFilmRating = filmService.topLikes(5);

        assertEquals(film5.getName(), testFilmRating.get(0).getName());
        assertEquals(film4.getName(), testFilmRating.get(1).getName());
        assertEquals(film3.getName(), testFilmRating.get(2).getName());
        assertEquals(film2.getName(), testFilmRating.get(3).getName());
        assertEquals(film1.getName(), testFilmRating.get(4).getName());


    }

}