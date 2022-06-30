package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.LikeRecordAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
// сброс кэша перед вызовом теста
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReviewTests {
    private final FilmService filmService;
    private final UserService userService;
    private final ReviewService reviewService;
    private static Film film1;
    private static User user1;
    private static User user2;
    private static User user3;

    @BeforeAll
    public static void init() {
        film1 = new Film("film1", "Description for Film1", LocalDate.of(1989, 7, 29), 90);
        film1.setId(1);
        film1.setMpa(new Mpa(1));

        user1 = new User("user1@ya.ru", "user1", LocalDate.of(1989, 7, 29));
        user1.setId(1);
        user1.setName("Пользователь 1");

        user2 = new User("user2@ya.ru", "user2", LocalDate.of(1989, 7, 29));
        user2.setId(2);
        user2.setName("Пользователь 2");

        user3 = new User("user3@ya.ru", "user3", LocalDate.of(1989, 7, 29));
        user3.setId(3);
        user3.setName("Пользователь 3");
    }

    @Test
    public void shouldThrowReviewNotFoundException() {
        filmService.addFilm(film1);
        userService.createUser(user1);

        Review review = Review.builder().content("Good film").isPositive(true).userId(1).filmId(1).build();
        reviewService.add(review);
        reviewService.deleteById(1);
        assertThrows(ReviewNotFoundException.class, () -> reviewService.findById(review.getId()));
    }

    @Test
    public void createdReviewAndFindReviewById() {
        filmService.addFilm(film1);
        userService.createUser(user1);

        Review review = Review.builder().content("Good film").isPositive(true).userId(1).filmId(1).build();
        reviewService.add(review);
        Review reviewToCompare = Review.builder().id(1).content("Good film").isPositive(true)
                .userId(1).filmId(1).useful(0).build();
        assertThat(reviewService.findById(1), is(equalTo(reviewToCompare)));
    }

    @Test
    public void updatedReviewFindReviewById() {
        filmService.addFilm(film1);
        userService.createUser(user1);

        Review review = Review.builder().content("Good film").isPositive(true).userId(1).filmId(1).build();
        reviewService.add(review);
        Review updateReview = Review.builder().id(1).content("Bad film").isPositive(false)
                .userId(1).filmId(1).build();
        reviewService.update(updateReview);
        Review reviewToCompare = Review.builder().id(1).content("Bad film").isPositive(false)
                .userId(1).filmId(1).useful(0).build();
        assertThat(reviewService.findById(1), is(equalTo(reviewToCompare)));
    }


    @Test
    public void checkReviewsWithFilmZeroReviews() {
        filmService.addFilm(film1);
        assertThat(reviewService.getForFilm(1, 10).size(), is(equalTo(0)));
    }

    @Test
    public void checkDefaultsForFilm() {
        filmService.addFilm(film1);
        userService.createUser(user1);

        Review review = Review.builder().content("Good film").isPositive(true).userId(1).filmId(1).build();
        reviewService.add(review);
        assertThat(reviewService.getForFilm(1, 10).size(), is(equalTo(1)));
    }

    @Test
    public void checkAfterAddLike() throws UserNotFoundException, ReviewNotFoundException,
            LikeRecordAlreadyExistsException {
        filmService.addFilm(film1);
        userService.createUser(user1);
        userService.createUser(user2);

        Review review = Review.builder().content("Good film").isPositive(true).userId(1).filmId(1).build();
        reviewService.add(review);
        reviewService.addLike(1, 2);
        Review reviewToCompare = Review.builder().id(1).content("Good film").isPositive(true)
                .userId(1).filmId(1).useful(1).build();
        assertThat(reviewService.findById(1), is(equalTo(reviewToCompare)));
    }

    @Test
    public void checkReviewAfterAddLikeOrDislike() throws UserNotFoundException, ReviewNotFoundException,
            LikeRecordAlreadyExistsException {
        filmService.addFilm(film1);
        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);

        Review review = Review.builder().content("Good film").isPositive(true).userId(1).filmId(1).build();
        reviewService.add(review);
        reviewService.addLike(1, 2);
        reviewService.addDislike(1, 3);
        Review reviewToCompare = Review.builder().id(1).content("Good film").isPositive(true)
                .userId(1).filmId(1).useful(0).build();
        assertThat(reviewService.findById(1), is(equalTo(reviewToCompare)));
    }

    @Test
    public void checkAfterAddLikeAndRemoveLike() throws UserNotFoundException, ReviewNotFoundException,
            LikeRecordAlreadyExistsException {
        filmService.addFilm(film1);
        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);

        Review review = Review.builder().content("Good film").isPositive(true).userId(1).filmId(1).build();
        reviewService.add(review);
        reviewService.addLike(1, 2);
        reviewService.removeLike(1, 2);
        Review reviewToCompare = Review.builder().id(1).content("Good film").isPositive(true)
                .userId(1).filmId(1).useful(0).build();
        assertThat(reviewService.findById(1), is(equalTo(reviewToCompare)));
    }

    @Test
    public void checkAfterAddDislikeOrRemoveDislike() throws UserNotFoundException, ReviewNotFoundException,
            LikeRecordAlreadyExistsException {
        filmService.addFilm(film1);
        userService.createUser(user1);
        userService.createUser(user2);

        Review review = Review.builder().content("Good film").isPositive(true).userId(1).filmId(1).build();
        reviewService.add(review);
        reviewService.addDislike(1, 2);
        reviewService.removeDislike(1, 2);
        Review reviewToCompare = Review.builder().id(1).content("Good film").isPositive(true)
                .userId(1).filmId(1).useful(0).build();
        assertThat(reviewService.findById(1), is(equalTo(reviewToCompare)));
    }

    @Test
    public void checkAfterAddDislike() throws UserNotFoundException, ReviewNotFoundException,
            LikeRecordAlreadyExistsException {
        filmService.addFilm(film1);
        userService.createUser(user1);
        userService.createUser(user2);

        Review review = Review.builder().content("Good film").isPositive(true).userId(1).filmId(1).build();
        reviewService.add(review);
        reviewService.addDislike(1, 2);
        Review reviewToCompare = Review.builder().id(1).content("Good film").isPositive(true)
                .userId(1).filmId(1).useful(-1).build();
        assertThat(reviewService.findById(1), is(equalTo(reviewToCompare)));
    }
}
