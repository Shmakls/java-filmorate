package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ReviewServiceTest {
    private final FilmService filmService;
    private final UserService userService;
    private final ReviewService reviewService;

    private static Film film1;
    private static Film film2;
    private static Film film3;
    private static User user1;
    private static User user2;
    private static User user3;
    private static User user4;

    @BeforeAll
    public static void beforeAllReviewDBStorageTests() {
        film1 = new Film("film1", "Description for Film1", LocalDate.of(1989, 7, 29), 90);
        film1.setId(1);
        film1.setMpa(new Mpa(1));

        film2 = new Film("film2", "Description for Film2", LocalDate.of(1989, 7, 29), 90);
        film2.setId(1);
        film2.setMpa(new Mpa(1));

        film3 = new Film("film3", "Description for Film3", LocalDate.of(1989, 7, 29), 90);
        film3.setId(1);
        film3.setMpa(new Mpa(1));

        user1 = new User("user1@ya.ru", "user1", LocalDate.of(1989, 7, 29));
        user1.setName("Пользователь 1");

        user2 = new User("user2@ya.ru", "user2", LocalDate.of(1989, 7, 29));
        user2.setName("Пользователь 2");

        user3 = new User("user3@ya.ru", "user3", LocalDate.of(1989, 7, 29));
        user3.setId(3);
        user3.setName("Пользователь 3");

        user4 = new User("user4@ya.ru", "user4", LocalDate.of(1989, 7, 29));
        user4.setName("Пользователь 4");
    }

    @Test
    public void reviewCreate() {
        filmService.addFilm(film1);
        userService.createUser(user1);

        assertDoesNotThrow(() -> {
            reviewService.add(Review.builder()
                    .content("Review 1")
                    .isPositive(true)
                    .filmId(film1.getId())
                    .userId(user1.getId())
                    .build());
        });
    }

    @Test
    public void reviewAlreadyExists() {
        assertThrows(ReviewAlreadyExistException.class,
                () -> reviewService.add(Review.builder()
                        .content("Review 1")
                        .isPositive(true)
                        .filmId(film1.getId())
                        .userId(user1.getId())
                        .build()));
    }

    @Test
    public void reviewCreateFailFilmId() {
        assertThrows(FilmNotFoundException.class, () -> reviewService.add(Review.builder()
                .content("Review 1")
                .isPositive(true)
                .filmId(Integer.MAX_VALUE)
                .userId(user1.getId())
                .build()));
    }

    @Test
    public void reviewCreateFailUserId() {
        assertThrows(UserNotFoundException.class, () -> reviewService.add(Review.builder()
                .content("Review 1")
                .isPositive(true)
                .filmId(film1.getId())
                .userId(Integer.MAX_VALUE)
                .build()));
    }

    @Test
    public void reviewUpdate() {
        userService.createUser(user2);

        Review review = reviewService.add(Review.builder()
                .content("Review ")
                .isPositive(true)
                .filmId(film1.getId())
                .userId(user2.getId())
                .build());

        String updatedContent = "Updated content";

        assertAll(
                () -> assertDoesNotThrow(() -> {
                    review.setContent(updatedContent);
                    reviewService.update(review);
                }),
                () -> assertEquals(reviewService.findById(review.getId()).getContent(), updatedContent),
                () -> assertThrows(ReviewNotFoundException.class, () -> {
                    review.setId(Integer.MAX_VALUE);
                    reviewService.update(review);
                })
        );
    }

    @Test
    public void getReviewById() {
        filmService.addFilm(film2);
        Review review = reviewService.add(Review.builder()
                .content("test ")
                .isPositive(true)
                .filmId(film2.getId())
                .userId(user1.getId())
                .build());

        assertAll(
                () -> assertEquals(review, reviewService.findById(review.getId())),
                () -> assertThrows(ReviewNotFoundException.class,
                        () -> reviewService.findById(Integer.MAX_VALUE))
        );
    }

    @Test
    public void deleteReviewById() {
        filmService.addFilm(film2);
        Review review = reviewService.add(Review.builder()
                .content("test ")
                .isPositive(true)
                .filmId(film2.getId())
                .userId(user2.getId())
                .build());

        reviewService.deleteById(review.getId());

        assertThrows(ReviewNotFoundException.class,
                () -> reviewService.findById(review.getId())
        );
    }

    @Test
    public void getAllReviewsByFilmId() {
        filmService.addFilm(film3);

        Review review1 = reviewService.add(Review.builder()
                .content("test ")
                .isPositive(true)
                .filmId(film3.getId())
                .userId(user2.getId())
                .build());

        Review review2 = reviewService.add(Review.builder()
                .content("test ")
                .isPositive(true)
                .filmId(film3.getId())
                .userId(user1.getId())
                .build());

        List<Review> checkList = List.of(review1, review2);

        assertEquals(checkList, reviewService.getAllByFilmId(film3.getId(), 10));
    }

    @Test
    public void addLikeOrDeleteLikeReview() {
        userService.createUser(user3);
        filmService.addFilm(film3);

        Review review = reviewService.add(Review.builder()
                .content("test ")
                .isPositive(true)
                .filmId(film3.getId())
                .userId(user3.getId())
                .build());

        assertAll(
                () -> {
                    reviewService.addLike(review.getId(), user3.getId());
                    assertEquals(reviewService.findById(review.getId()).getUseful(), 1);
                },
                () -> assertThrows(UsefulAlreadyExistsException.class,
                        () -> reviewService.addLike(review.getId(), user3.getId())
                ),
                () -> {
                    reviewService.deleteLikeReview(review.getId(), user3.getId());
                    assertEquals(reviewService.findById(review.getId()).getUseful(), 0);
                }
        );
    }

    @Test
    public void addDislikeOrDeleteDislikeReview() {
        userService.createUser(user4);
        filmService.addFilm(film3);

        Review review = reviewService.add(Review.builder()
                .content("test ")
                .isPositive(true)
                .filmId(film3.getId())
                .userId(user4.getId())
                .build());

        assertAll(
                () -> {
                    reviewService.addDislike(review.getId(), user4.getId());
                    assertEquals(reviewService.findById(review.getId()).getUseful(), -1);
                },
                () -> assertThrows(UsefulAlreadyExistsException.class,
                        () -> reviewService.addDislike(review.getId(), user4.getId())
                ),
                () -> {
                    reviewService.deleteDislike(review.getId(), user4.getId());
                    assertEquals(reviewService.findById(review.getId()).getUseful(), 0);
                }
        );
    }
}