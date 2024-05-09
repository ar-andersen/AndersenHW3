package com.rybak.andersenhw3.dao;

import com.rybak.andersenhw3.config.DataSource;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.TaskManagerGlobalException;
import com.rybak.andersenhw3.util.MapperUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDao {

    private static final String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    private static final String INSERT_USER = "INSERT INTO users (id, name, email, password, role) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?";
    private static final String GET_ALL_USERS = "SELECT * FROM users";

    public boolean existsByEmail(String email) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_EMAIL)) {

            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    public void insertUser(User user) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER)) {

            preparedStatement.setObject(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getRole().name());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    public User findUserByEmail(String email) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_EMAIL)) {
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            return MapperUtil.getUser(resultSet);
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    public User findUserById(UUID id) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_ID)) {
            preparedStatement.setObject(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            return MapperUtil.getUser(resultSet);
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    public boolean deleteUserById(UUID id) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID)) {
            preparedStatement.setObject(1, id);

            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USERS)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            return MapperUtil.getUserList(resultSet);
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

}
