package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage.ReviewStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ReviewDBStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String FIND_BY_ID = "SELECT * FROM REVIEWS WHERE REVIEW_ID = ?";
    private static final String UPDATE_REVIEW = "UPDATE REVIEWS SET CONTENT = ?, IS_POSITIVE = ? WHERE REVIEW_ID = ?";
    private static final String GET_REVIEW_USEFUL_BY_ID = "SELECT SUM(USEFUL) AS USEFUL_SUM FROM REVIEW_USEFULS WHERE REVIEW_ID = ?";
    private static final String ADD_USEFUL_TO_REVIEW = "INSERT INTO REVIEW_USEFULS(REVIEW_ID, USER_ID, USEFUL) VALUES (?, ?, ?);";
    private static final String GET_ALL_REVIEWS_BY_FILM_ID = "SELECT * FROM REVIEWS WHERE FILM_ID = ? LIMIT ?";
    private static final String DELETE_REVIEW_USEFUL = "DELETE FROM REVIEW_USEFULS WHERE REVIEW_ID = ? AND USER_ID = ? AND USEFUL = ?";
    private static final String SQL_FOR_DELETE_REVIEW_BY_ID = "DELETE FROM REVIEWS WHERE REVIEW_ID = ?";

    public ReviewDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review add(Review review) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("review_id");
        Map<String, Object> values = new HashMap<>();
        values.put("content", review.getContent());
        values.put("user_id", review.getUserId());
        values.put("film_id", review.getFilmId());
        values.put("is_positive", review.isPositive());
        try {
            int reviewId = insert.executeAndReturnKey(values).intValue();
            review.setId(reviewId);
        } catch (DataIntegrityViolationException e) {
            String message = e.getMessage();
            assert message != null;
            if (message.contains("FK_REVIEWS_USER_ID")) {
                throw new UserNotFoundException("User not found");
            } else if (message.contains("FK_REVIEWS_FILM_ID")) {
                throw new FilmNotFoundException("Film not found");
            } else if (message.contains("UNIQUE_USER_ID_FILM_ID_REVIEWS")) {
                throw new ReviewAlreadyExistException("Review already exists");
            }
        }
        return review;
    }

    @Override
    public Review update(Review review) {
        int queryResult = jdbcTemplate.update(
                UPDATE_REVIEW,
                review.getContent(),
                review.isPositive(),
                review.getId()
        );
        if (queryResult == 0) {
            throw new ReviewNotFoundException("Review with id:" + review.getId() + " not found");
        }
        return review;
    }

    @Override
    public Review findById(int id) {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID, this::mapRowToReview, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ReviewNotFoundException("Review with id:" + id + " not found");
        }
    }

    private Review mapRowToReview(ResultSet rs, int rowNum) throws SQLException {
        return Review.builder()
                .id(rs.getInt("review_id"))
                .userId(rs.getInt("user_id"))
                .filmId(rs.getInt("film_id"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("is_positive"))
                .useful(getUsefulById(rs.getInt("review_id")))
                .build();
    }

    @Override
    public List<Review> getAllByFilmId(int filmId, int count) {
        return jdbcTemplate.query(GET_ALL_REVIEWS_BY_FILM_ID, this::mapRowToReview, filmId, count);
    }

    @Override
    public Review addUseful(int reviewId, int userId, int value) {
        try {
            jdbcTemplate.update(ADD_USEFUL_TO_REVIEW, reviewId, userId, value);
        } catch (DataIntegrityViolationException e) {
            String message = e.getMessage();

            assert message != null;
            if (message.contains("PRIMARY_KEY")) {
                throw new UsefulAlreadyExistsException("Already exists useful");
            } else if (message.contains("FK_REVIEW_USEFULS_USER_ID")) {
                throw new UserNotFoundException("User not found");
            } else if (message.contains("FK_REVIEW_USEFULS_ID")){
                throw new ReviewNotFoundException("Review not found");
            }
        }
        return findById(reviewId);
    }

    @Override
    public Review deleteUseful(int reviewId, int userId, int value) {
        int deleteResult = jdbcTemplate.update(DELETE_REVIEW_USEFUL, reviewId, userId, value);

        if (deleteResult == 0) {
            throw new UsefulNotFoundException("Useful not found");
        }
        return findById(reviewId);
    }

    @Override
    public Map<String, String> removeById(int id) {
        int deleteResult = jdbcTemplate.update(SQL_FOR_DELETE_REVIEW_BY_ID, id);

        if (deleteResult > 0) {
            return Map.of("message", "review with id:" + id + " deleted");
        } else {
            throw new ReviewNotFoundException("Review not found");
        }
    }

    private Integer getUsefulById(int id) {
        return jdbcTemplate.queryForObject(GET_REVIEW_USEFUL_BY_ID,
                (ResultSet rs, int rowNum) -> rs.getInt("useful_sum"),
                id);
    }
}
