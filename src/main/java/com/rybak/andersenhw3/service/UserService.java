package com.rybak.andersenhw3.service;

import com.rybak.andersenhw3.dao.UserDao;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUserById(UUID id) {
        User user = userDao.findUserById(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("User with id %s doesn't exists", id));
        }

        return user;
    }

    public boolean deleteUserById(UUID id) {
        return userDao.deleteUserById(id);
    }

    public User getUserByEmail(String email) {
        User user = userDao.findUserByEmail(email);

        if (user == null) {
            throw new UserNotFoundException(String.format("User with email %s doesn't exists", email));
        }

        return user;
    }

}
