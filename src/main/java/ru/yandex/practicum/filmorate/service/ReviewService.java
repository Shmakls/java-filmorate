package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage.ReviewStorage;

import java.util.List;
import java.util.Map;

@Service
public class ReviewService {
    private static final int USEFUL_CHANGED = 1;
    private final ReviewStorage reviewStorage;

    @Autowired
    public ReviewService(@Qualifier("reviewDBStorage") ReviewStorage reviewStorage) {
        this.reviewStorage = reviewStorage;
    }

    public Review add(Review review) {
        return reviewStorage.add(review);
    }

    public Review update(Review review) {
        return reviewStorage.update(review);
    }

    public Map<String, String> deleteById(int id) {
        return reviewStorage.removeById(id);
    }

    public Review findById(int id) {
        return reviewStorage.findById(id);
    }

    public List<Review> getAllByFilmId(int id, int count) {
        return reviewStorage.getAllByFilmId(id, count);
    }

    public Review addLike(int reviewId, int userId) {
        return reviewStorage.addUseful(reviewId, userId, USEFUL_CHANGED);
    }

    public Review addDislike(int reviewId, int userId) {
        return reviewStorage.addUseful(reviewId, userId, -USEFUL_CHANGED);
    }

    public Review deleteLikeReview(int reviewId, int userId) {
        return reviewStorage.deleteUseful(reviewId, userId, USEFUL_CHANGED);
    }

    public Review deleteDislike(int reviewId, int userId) {
        return reviewStorage.deleteUseful(reviewId, userId, -USEFUL_CHANGED);
    }
}