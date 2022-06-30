package ru.yandex.practicum.filmorate.storage.ReviewStorage;

public interface ReviewUsefulStorage {
    boolean isInUseful(Integer reviewId, Integer userId);

    void addLike(Integer reviewId, Integer userId);

    void removeLike(Integer reviewId, Integer userId);

    void addDislike(Integer reviewId, Integer userId);

    void removeDislike(Integer reviewId, Integer userId);
}
