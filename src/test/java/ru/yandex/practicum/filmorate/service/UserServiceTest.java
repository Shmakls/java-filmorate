package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.IncorrectIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    UserService userService;

    User user1;
    User user2;
    User user3;
    User user4;
    User user5;

    @BeforeEach
    void beforeEach() {

        userService = new UserService(new InMemoryUserStorage());

        user1 = new User("user1@ya.ru", "user1", LocalDate.of(1989, 7, 29));
        user1.setName("Пользователь 1");

        user2 = new User("user2@ya.ru", "user2", LocalDate.of(1989, 7, 29));
        user2.setName("Пользователь 2");

        user3 = new User("user3@ya.ru", "user3", LocalDate.of(1989, 7, 29));
        user3.setId(3);
        user3.setName("Пользователь 3");

        user4 = new User("user4@ya.ru", "user4", LocalDate.of(1989, 7, 29));
        user4.setName("Пользователь 4");

        user5 = new User("user5@ya.ru", "user5", LocalDate.of(1989, 7, 29));
        user5.setName("Пользователь 5");

    }

    @Test
    void shouldBeAddUserToRep() {

        userService.createUser(user1);
        userService.createUser(user2);

        assertEquals(2, userService.findAll().size());

    }

    @Test
    void shouldBeAddUserWhenUpdate() {

        userService.createUser(user1);
        userService.createUser(user2);

        userService.updateUser(user3);

        User testUser = userService.getUserById(3);

        assertEquals(user3.getLogin(), testUser.getLogin());

    }

    @Test
    void shouldBeUpdateUser() {

        userService.createUser(user1);
        userService.createUser(user2);

        User updateUser2 = new User("updateUser2@ya.ru", "UpdateUser2", LocalDate.of(1989, 7, 29));
        updateUser2.setName("Пользователь обновленный 2");
        updateUser2.setId(2);

        userService.updateUser(updateUser2);

        assertEquals(updateUser2.getLogin(), userService.getUserById(2).getLogin());

    }

    @Test
    void shouldBeGetUserById() {

        userService.createUser(user1);

        User testUser = userService.getUserById(1);

        assertEquals(user1.getLogin(), testUser.getLogin());

    }

    @Test
    void shouldBeThrowExceptionIfUserIdIsNotExistByGetUser() {

        userService.createUser(user1);

        final IncorrectIdException exception =  assertThrows(
                    IncorrectIdException.class,
                    () -> userService.getUserById(3)
        );

    }

    @Test
    void shouldBeAddUserToFriend() {

        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);

        userService.addFriend(1, 3);

        assertTrue(userService.getUserById(1).getFriends().contains(3));
        assertTrue(userService.getUserById(3).getFriends().contains(1));
    }

    @Test
    void shouldBeThrowExceptionIfUserIsAlreadyFriendly() {

        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);

        userService.addFriend(1, 3);

        final IncorrectIdException exception = assertThrows(
                IncorrectIdException.class,
                () -> userService.addFriend(3, 1)
        );

    }

    @Test
    void shouldBeRemoveUserFromFriend() {

        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);

        userService.addFriend(1, 3);

        assertTrue(userService.getUserById(1).getFriends().contains(3));
        assertTrue(userService.getUserById(3).getFriends().contains(1));

        userService.deleteFromFriends(1, 3);

    }

    @Test
    void shouldBeThrowExceptionIfUsersNotFriendByDelete() {

        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);

        final IncorrectIdException exception = assertThrows(
                IncorrectIdException.class,
                () -> userService.deleteFromFriends(1, 1)
        );

    }

    @Test
    void shouldBeDeleteUser() {

        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);

        userService.deleteUser(2);

        assertEquals(2, userService.findAll().size());

    }

    @Test
    void shouldBeThrowExceptionIfDeleteUserIdIsNotExist() {

        userService.createUser(user1);
        userService.createUser(user2);

        final IncorrectIdException exception = assertThrows(
                IncorrectIdException.class,
                () -> userService.deleteUser(3)
        );

    }

    @Test
    void shouldBeGetFriendLoginList() {

        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);
        userService.createUser(user4);
        userService.createUser(user5);

        userService.addFriend(1, 3);
        userService.addFriend(1, 5);

        List<User> testListFriends = userService.getFriendList(1);

        assertEquals(user3.getLogin(), testListFriends.get(0).getLogin());
        assertEquals(user5.getLogin(), testListFriends.get(1).getLogin());

    }

    @Test
    void shouldBeGetCommonFriendList() {

        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);
        userService.createUser(user4);
        userService.createUser(user5);

        userService.addFriend(1, 2);
        userService.addFriend(1, 4);
        userService.addFriend(5, 2);
        userService.addFriend(5, 4);
        userService.addFriend(5, 3);

        List<User> testCommonFriendsList = userService.getCommonFriendsListAsLogins(1, 5);

        assertEquals(2, testCommonFriendsList.size());

        assertEquals(user2.getLogin(), testCommonFriendsList.get(0).getLogin());
        assertEquals(user4.getLogin(), testCommonFriendsList.get(1).getLogin());

    }

    @Test
    void shouldBeGetAllUsers() {

        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);
        userService.createUser(user4);
        userService.createUser(user5);

        assertEquals(5, userService.findAll().size());

    }

}