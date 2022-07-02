package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage.FilmStorage;
import ru.yandex.practicum.filmorate.validators.DirectorValidator;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.time.Year;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    private final FilmValidator filmValidator;

    private Integer id = 0;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, UserService userService) {
        this.userService = userService;
        this.filmStorage = filmStorage;
        filmValidator = new FilmValidator();
    }

    public Film addFilm(Film film) {

        filmValidator.isValid(film);

        film.setId(++id);

        return filmStorage.save(film);

    }

    public Film updateFilm(Film film) {

        filmValidator.isValid(film);

        if (film.getId() < 1) {
            throw new IncorrectIdException("ID не может быть отрицательным");
        }

        return filmStorage.update(film);

    }

    public void deleteFilm(Integer id) {

        if (!filmStorage.isContains(id)) {
            throw new IncorrectIdException("Такой ID не сушествует.");
        }

        filmStorage.delete(id);
    }

    public Film getFilmById(Integer id) {

        if (!filmStorage.isContains(id)) {
            throw new IncorrectIdException("Такой ID не сушествует.");
        }

        return filmStorage.getFilmById(id);

    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public String addLike(Integer id, Integer userId) {

        if (!filmStorage.isContains(id)) {
            throw new IncorrectIdException("Такого фильма не существует");
        }

        Film film = filmStorage.getFilmById(id);

        Set<Integer> likes = film.getLikes();

        if (!likes.add(userId)) {
            throw new AlreadyExistException("Пользователь уже лайкнул этот фильм");
        }

        filmStorage.update(film);

        return ("Пользователь " + userService.getUserById(userId).getLogin() + " поставил лайк фильму "
                + filmStorage.getFilmById(id).getName() + ".");

    }

    public String deleteLike(Integer id, Integer userId) {

        if (!filmStorage.isContains(id)) {
            throw new IncorrectIdException("Такого фильма не существует");
        }

        Film film = filmStorage.getFilmById(id);

        if (!film.getLikes().contains(userId)) {
            throw new IncorrectIdException("Этот пользователь не ставил лайк этому фильму");
        }

        film.getLikes().remove(userId);

        updateFilm(film);

        return ("Пользователь " + userService.getUserById(userId).getLogin() + " убрал лайк у фильма "
                + filmStorage.getFilmById(id).getName() + ".");

    }

    // Получение топ N фильмов. В случае если genreId и/или year > 0 применяем фильтрацию по ним.
    public List<Film> topLikes(int count, int genreId, int year) {
        if (genreId > 0 || year > 0) {
            return getTopFilmsByFilter(count, genreId, year);
        } else if (genreId == 0 && year == 0) {
            List<Integer> topFilmsId = filmStorage.getTopFilms(count);

            if (!topFilmsId.isEmpty()) {
                return topFilmsId.stream()
                        .map(this::getFilmById)
                        .collect(Collectors.toList());
            }
        }

        return filmStorage.findAll().stream()
                .limit(count)
                .collect(Collectors.toList());

    }

    // Получение фильмов с учетом жанра и/или года
    private List<Film> getTopFilmsByFilter(int count, int genreId, int year) {
        List<Integer> topFilteredIds;

        // Если задан год - получаем топ фильмов. Иначе только по жанру
        if (year > 0) {

            // Если год меньше начала создания фильмов или больше текущего -> ошибка
            if (year < 1895 || year > Year.now().getValue()) {
                throw new ValidationException("Некорректный год сортировки");
            }

            topFilteredIds = filmStorage.getTopYearFilm(year);

            // Если задан жанр фильтруем по нему
            if (genreId > 0) {
                return topFilteredIds.stream()
                        .limit(count)
                        .map(this::getFilmById)
                        .filter((film) -> film.getGenres().contains(getGenreById(genreId)))
                        .collect(Collectors.toList());
            }

        } else {
            topFilteredIds = filmStorage.getTopGenreFilm(genreId);
        }

        return topFilteredIds.stream()
                .limit(count)
                .map(this::getFilmById)
                .collect(Collectors.toList());
    }

    public List<Genre> findAllGenres() {
        return filmStorage.getAllGenres();
    }

    public Genre getGenreById(Integer id) {

        List<Genre> allGenres = findAllGenres();

        if (id > allGenres.size() || id < 1) {
            throw new IncorrectIdException("Некорректиный Id жанра");
        }

        return allGenres.stream().filter(x -> x.getId() == id).findFirst().get();

    }

    public Mpa getMpaById(Integer id) {

        List<Mpa> allMpa = findAllMpa();

        if (id > allMpa.size() || id < 1) {
            throw new IncorrectIdException("Некорректный Id рейтинга");
        }

        return allMpa.stream().filter(x -> x.getId() == id).findFirst().get();

    }

    public List<Mpa> findAllMpa() {
        return filmStorage.getAllMpa();
    }

    public List<Film> getCommonFilms(Integer user1, Integer user2) {
        return filmStorage.getCommonFilms(user1, user2);
    }

    public List<Director> findAllDirectors() {
        return filmStorage.getAllDirectors();
    }
    public Director getDirectorById(Integer id) {
        List<Director> allDirectors = findAllDirectors();

        if (id > allDirectors.size() || id < 1) {
            throw new IncorrectIdException("Некорректный Id режиссера");
        }

        return allDirectors
                .stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .get();
    }

    public Director createDirector(Director director) {
        DirectorValidator.isValid(director);
        return filmStorage.createDirector(director);
    }

    public Director updateDirector(Director director) {
        DirectorValidator.isValid(director);

        if (director.getId() > findAllDirectors().size() || director.getId() < 1) {
            throw new IncorrectIdException("Некорректный Id режиссера");
        }

        return filmStorage.updateDirector(director);
    }

    public void removeDirector(Integer id) {
        if (id > findAllDirectors().size() || id < 1) {
            throw new IncorrectIdException("Некорректный Id режиссера");
        }

        filmStorage.removeDirector(id);
    }

    public List<Film> getTopFilmsByDirector(Integer id, String sortBy) {
        if (id > findAllDirectors().size() || id < 1) {
            throw new IncorrectIdException("Некорректный Id режиссера");
        }

        return filmStorage
                .getFilmsByDirectorSorted(id, sortBy)
                .stream()
                .map(this::getFilmById)
                .collect(Collectors.toList());
    }
}