package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage.ReviewStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * Класс, имплементирующий интерфейс для работы с таблицей review в БД
 */
@Component
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;
    String ADD_REVIEW = "INSERT INTO REVIEW (CONTENT, IS_POSITIVE, USER_ID, FILM_ID) VALUES (?, ?, ?, ?)";
    String FIND_REVIEW = "SELECT *, (SELECT SUM(case when IS_LIKE = true then 1 else -1 end) FROM REVIEW_USEFUL "
            + "WHERE REVIEW_ID = R.REVIEW_ID AND IS_DELETE = FALSE) as USEFUL FROM REVIEW R "
            + "WHERE R.REVIEW_ID = ? AND (NOT R.IS_DELETE)";
    String UPDATE_REVIEW = "UPDATE review SET CONTENT = ?, IS_POSITIVE = ? WHERE REVIEW_ID = ?";
    String GET_FOR_FILM = "SELECT *, (SELECT SUM(case when IS_LIKE = true then 1 else -1 end) FROM REVIEW_USEFUL "
            + "WHERE REVIEW_ID = r.REVIEW_ID) as useful FROM review r WHERE r.FILM_ID = ? "
            + "AND (NOT IS_DELETE) LIMIT ?";
    String ALL_REVIEWS = "SELECT *, (SELECT SUM(case when IS_LIKE = true then 1 else -1 end) FROM REVIEW_USEFUL "
            + "WHERE REVIEW_ID = r.REVIEW_ID) as useful FROM review r WHERE "
            + "(NOT IS_DELETE) LIMIT ?";
    String DELETE_REVIEW = "UPDATE review SET IS_DELETE = true WHERE REVIEW_ID = ?";

    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review add(Review review) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(ADD_REVIEW, new String[]{"REVIEW_ID"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setLong(3, review.getUserId());
            stmt.setLong(4, review.getFilmId());
            return stmt;
        }, keyHolder);

        int key = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return findById(key);
    }

    @Override
    public Review findById(Integer id) {
        return jdbcTemplate.query(FIND_REVIEW, this::mapRowToReview, id).stream()
                .findAny()
                .orElseThrow(() -> new ReviewNotFoundException("Review not found!"));
    }

    @Override
    public Review update(Review review) {
        jdbcTemplate.update(UPDATE_REVIEW,
                review.getContent(),
                review.getIsPositive(),
                review.getId());

        return findById(review.getId());
    }

    @Override
    public List<Review> getForFilm(Integer id, int count) {
        return jdbcTemplate.query(GET_FOR_FILM, this::mapRowToReview, id, count);
    }

    @Override
    public List<Review> getAllReviews(Integer count) {
        return jdbcTemplate.query(ALL_REVIEWS, this::mapRowToReview, count);
    }

    @Override
    public void deleteById(Integer reviewId) {
        jdbcTemplate.update(DELETE_REVIEW, reviewId);
    }

    @Override
    public boolean isExists(Integer id) {
        return !(findById(id) == null);
    }

    private Review mapRowToReview(ResultSet resultSet, int rowNum) throws SQLException {
        return Review.builder()
                .id(resultSet.getInt("REVIEW_ID"))
                .content(resultSet.getString("CONTENT"))
                .isPositive(resultSet.getBoolean("IS_POSITIVE"))
                .userId(resultSet.getInt("USER_ID"))
                .filmId(resultSet.getInt("FILM_ID"))
                .useful(resultSet.getInt("USEFUL"))
                .build();
    }
}