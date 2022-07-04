package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.ReviewStorage.ReviewUsefulStorage;

@Component
public class ReviewUsefulDbStorage implements ReviewUsefulStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String ADD_LIKE = "INSERT INTO REVIEW_USEFUL (REVIEW_ID, USER_ID, IS_LIKE) VALUES (?, ?, true)";
    private static final String REMOVE_LIKE = "UPDATE REVIEW_USEFUL SET IS_DELETE = true WHERE REVIEW_ID = ? AND USER_ID = ?";
    private static final String ADD_DISLIKE = "INSERT INTO REVIEW_USEFUL (REVIEW_ID, USER_ID, IS_LIKE) VALUES (?, ?, false)";
    private static final String REMOVE_DISLIKE = "UPDATE REVIEW_USEFUL SET IS_DELETE = true WHERE REVIEW_ID = ? AND USER_ID = ? ";
    private static final String CHECK_USEFUL = "SELECT COUNT(*) FROM REVIEW_USEFUL " +
            "WHERE REVIEW_ID = ? AND USER_ID = ? AND IS_DELETE = false";

    public ReviewUsefulDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(Integer reviewId, Integer userId) {
        jdbcTemplate.update(ADD_LIKE, reviewId, userId);
    }

    @Override
    public void removeLike(Integer reviewId, Integer userId) {
        jdbcTemplate.update(REMOVE_LIKE, reviewId, userId);
    }

    @Override
    public void addDislike(Integer reviewId, Integer userId) {
        jdbcTemplate.update(ADD_DISLIKE, reviewId, userId);
    }

    @Override
    public void removeDislike(Integer reviewId, Integer userId) {
        jdbcTemplate.update(REMOVE_DISLIKE, reviewId, userId);
    }

    @Override
    public boolean isInUseful(Integer reviewId, Integer userId) {
        Integer integer = jdbcTemplate.queryForObject(CHECK_USEFUL, Integer.class, reviewId, userId);
        assert integer != null;
        return integer != 0;
    }
}
