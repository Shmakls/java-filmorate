package ru.yandex.practicum.filmorate.storage.ReviewStorage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    Review add(Review review);

    Review findById(Integer id);

    Review update(Review review);

    List<Review> getForFilm(Integer id, int count);

    List<Review> getAllReviews(Integer count);

    void deleteById(Integer reviewId);
}
