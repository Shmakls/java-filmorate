package ru.yandex.practicum.filmorate.storage.ReviewStorage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Map;

public interface ReviewStorage {
    Review add(Review review);

    Review update(Review review);

    Map<String, String> removeById(int id);

    Review findById(int id);

    List<Review> getAllByFilmId(int filmId, int count);

    Review addUseful(int reviewId, int userId, int value);

    Review deleteUseful(int reviewId, int userId, int value);
}
