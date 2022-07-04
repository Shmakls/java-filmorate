package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage.ReviewUsefulStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage.UserStorage;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class ReviewService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final ReviewStorage reviewStorage;
    private final ReviewUsefulStorage reviewUsefulStorage;

    public ReviewService(@Qualifier("userDbStorage") UserStorage userStorage,
                         @Qualifier("filmDbStorage") FilmStorage filmStorage,
                         @Qualifier("reviewDbStorage") ReviewStorage reviewStorage,
                         @Qualifier("reviewUsefulDbStorage")ReviewUsefulStorage reviewUsefulStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.reviewStorage = reviewStorage;
        this.reviewUsefulStorage = reviewUsefulStorage;
    }

    public Review add(Review review) throws UserNotFoundException, FilmNotFoundException, ValidationException {
        if (review.getUserId() == null || review.getFilmId() == null || review.getIsPositive() == null) {
            throw new ValidationException("Bad request");
        } else {
            User user = userStorage.getUserById(review.getUserId());
            Film film = filmStorage.getFilmById(review.getFilmId());
            if (film == null) {
                throw new FilmNotFoundException("Film not found");
            } else if (user == null) {
                throw new UserNotFoundException("User not found");
            } else {
                return reviewStorage.add(review);
            }
        }
    }

    public Review update(Review review) throws ReviewNotFoundException {
        if (!reviewStorage.isExists(review.getId())) {
            throw new ReviewNotFoundException("Review not found");
        } else {
            return reviewStorage.update(review);
        }
    }

    public Review findById(Integer id) throws ReviewNotFoundException {
        if (!reviewStorage.isExists(id)) {
            throw new ReviewNotFoundException("Review not found");
        } else {
            return reviewStorage.findById(id);
        }
    }

    public List<Review> getForFilm(Integer id, int count) throws FilmNotFoundException {
        if (id == null) {
            List<Review> getReviews = reviewStorage.getAllReviews(count);
            getReviews.sort(Comparator.comparing(Review::getUseful).reversed()
                    .thenComparing(Review::getId));
            return getReviews;
        } else {
            if (!filmStorage.isExists(id)) {
                throw new FilmNotFoundException("Film not found");
            } else {
                List<Review> getReviews = reviewStorage.getForFilm(id, count);
                getReviews.sort(Comparator.comparing(Review::getUseful).reversed()
                                .thenComparing(Review::getId));
                return getReviews;
            }
        }
    }

    public void addLike(Integer reviewId, Integer userId) throws ReviewNotFoundException, UserNotFoundException,
            LikeRecordAlreadyExistsException {
        if (!userStorage.isExists(userId)) {
            throw new UserNotFoundException("User not found");
        } else if (!reviewStorage.isExists(reviewId)) {
            throw new ReviewNotFoundException("Review not found");
        } else if (reviewUsefulStorage.isInUseful(reviewId, userId)) {
            throw new LikeRecordAlreadyExistsException("Review already exists");
        } else {
            reviewUsefulStorage.addLike(reviewId, userId);
        }
    }

    public void removeLike(Integer reviewId, Integer userId) throws UserNotFoundException,
            ReviewNotFoundException {
        if (!userStorage.isExists(userId)) {
            throw new UserNotFoundException("User not found");
        } else if (!reviewStorage.isExists(reviewId)) {
            throw new ReviewNotFoundException("Review not found");
        } else {
            reviewUsefulStorage.removeLike(reviewId, userId);
        }
    }

    public void addDislike(Integer reviewId, Integer userId) throws UserNotFoundException, ReviewNotFoundException,
            LikeRecordAlreadyExistsException {
        if (!userStorage.isExists(userId)) {
            throw new UserNotFoundException("User not found");
        } else if (!reviewStorage.isExists(reviewId)) {
            throw new ReviewNotFoundException("Review not found");
        } else if (reviewUsefulStorage.isInUseful(reviewId, userId)) {
            throw new LikeRecordAlreadyExistsException("Review already exists");
        } else {
            reviewUsefulStorage.addDislike(reviewId, userId);
        }
    }

    public void removeDislike(Integer reviewId, Integer userId) throws UserNotFoundException,
            ReviewNotFoundException {
        if (!userStorage.isExists(userId)) {
            throw new UserNotFoundException("User not found");
        } else if (!reviewStorage.isExists(reviewId)) {
            throw new ReviewNotFoundException("Review not found");
        } else {
            reviewUsefulStorage.removeDislike(reviewId, userId);
        }
    }

    public void deleteById(Integer reviewId) throws ReviewNotFoundException {
        if (!reviewStorage.isExists(reviewId)) {
            throw new ReviewNotFoundException("Review not found");
        } else {
            reviewStorage.deleteById(reviewId);
        }
    }
}
