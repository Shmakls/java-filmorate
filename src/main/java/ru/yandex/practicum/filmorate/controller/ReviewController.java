package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<Review> getAllByFilmId(
            @RequestParam(required = false) Integer film,
            @RequestParam(defaultValue = "10", required = false) Integer count) {
        if (film == null) {
            throw new IncorrectParameterException("Query param <film> incorrect");
        }
        return reviewService.getAllByFilmId(film, count);
    }

    @PostMapping
    public Review add(@Valid @RequestBody Review review) {
        return reviewService.add(review);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        return reviewService.update(review);
    }

    @DeleteMapping("{id}")
    public Map<String, String> deleteById(@PathVariable(required = false) Integer id) {
        if (id == null) {
            throw new IncorrectParameterException("Review id incorrect");
        }

        return reviewService.deleteById(id);
    }

    @GetMapping("{id}")
    public Review findById(@PathVariable(required = false) Integer id) {
        if (id == null) {
            throw new IncorrectParameterException("Review id incorrect");
        }
        return reviewService.findById(id);
    }

    @PutMapping("{id}/like/{userId}")
    public Review addLike(
            @PathVariable(required = false) Integer id,
            @PathVariable(required = false) Integer userId) {
        checkIds(id, userId);
        return reviewService.addLike(id, userId);
    }

    @PutMapping("{id}/dislike/{userId}")
    public Review addDislike(
            @PathVariable(required = false) Integer id,
            @PathVariable(required = false) Integer userId) {
        checkIds(id, userId);
        return reviewService.addDislike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Review deleteLike(
            @PathVariable(required = false) Integer id,
            @PathVariable(required = false) Integer userId) {
        checkIds(id, userId);
        return reviewService.deleteLikeReview(id, userId);
    }

    @DeleteMapping("{id}/dislike/{userId}")
    public Review deleteDislike(
            @PathVariable(required = false) Integer id,
            @PathVariable(required = false) Integer userId) {
        checkIds(id, userId);
        return reviewService.deleteDislike(id, userId);
    }

    private void checkIds(Integer id, Integer userId) {
        if (id == null) {
            throw new IncorrectParameterException("Review id incorrect");
        }
        if (userId == null) {
            throw new IncorrectParameterException("User id incorrect");
        }
    }
}