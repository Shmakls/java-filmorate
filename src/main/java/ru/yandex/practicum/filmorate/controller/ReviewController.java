package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

/**
 * Класс-контроллер для работы с отзывами
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public List<Review> getForFilm(@RequestParam(required = false) Integer filmId,
                                   @RequestParam(defaultValue = "10") int count) throws FilmNotFoundException {
        return reviewService.getForFilm(filmId, count);
    }

    @GetMapping("/{reviewId}")
    public Review findById(@PathVariable Integer reviewId) throws ReviewNotFoundException {
        return reviewService.findById(reviewId);
    }

    @PostMapping
    public Review add(@RequestBody Review review) throws UserNotFoundException, FilmNotFoundException,
            ValidationException {
        return reviewService.add(review);
    }

    @PutMapping
    public Review update(@RequestBody Review review) throws ValidationException, FilmNotFoundException,
            ReviewNotFoundException {
        return reviewService.update(review);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteById(@PathVariable Integer reviewId) throws ReviewNotFoundException {
        reviewService.deleteById(reviewId);
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addLike(@PathVariable Integer reviewId, @PathVariable Integer userId)
            throws UserNotFoundException, ReviewNotFoundException, LikeRecordAlreadyExistsException {
        reviewService.addLike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void removeLike(@PathVariable Integer reviewId, @PathVariable Integer userId)
            throws UserNotFoundException, ReviewNotFoundException {
        reviewService.removeLike(reviewId, userId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDislike(@PathVariable Integer reviewId, @PathVariable Integer userId)
            throws UserNotFoundException, ReviewNotFoundException, LikeRecordAlreadyExistsException {
        reviewService.addDislike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void removeDislike(@PathVariable Integer reviewId, @PathVariable Integer userId)
            throws UserNotFoundException, ReviewNotFoundException {
        reviewService.removeDislike(reviewId, userId);
    }
}
