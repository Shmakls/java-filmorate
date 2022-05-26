package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage.UserStorage;

public class Context {

    private static UserStorage USER_STORAGE = new InMemoryUserStorage();

    private static FilmStorage FILM_STORAGE = new InMemoryFilmStorage();


    public static UserService USER_SERVICE = new UserService(USER_STORAGE);

    public static FilmService FILM_SERVICE = new FilmService(FILM_STORAGE, USER_SERVICE);

}
