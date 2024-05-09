package com.rybak.andersenhw3.dao;

import com.rybak.andersenhw3.config.DataSource;
import com.rybak.andersenhw3.entity.Task;
import com.rybak.andersenhw3.exception.TaskManagerGlobalException;
import com.rybak.andersenhw3.util.MapperUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class TaskDao {

    private static final String INSERT_TASK = "INSERT INTO tasks (id, title, description, status, assignee_id, reporter_id, project_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_TASK_BY_ID_AND_PROJECT_ID = "SELECT * FROM tasks WHERE id = ? AND project_id = ?";
    private static final String GET_ALL_TASKS_BY_PROJECT_ID = "SELECT * FROM tasks WHERE project_id = ?";
    private static final String DELETE_TASK = "DELETE FROM tasks WHERE id = ?";
    private static final String UPDATE_TASK_BY_ID = "UPDATE tasks SET title = ?, description = ?, status = ?, assignee_id = ?, reporter_id = ? WHERE id = ?";

    public void saveTask(Task task, UUID projectId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TASK)) {
            preparedStatement.setObject(1, task.getId());
            preparedStatement.setString(2, task.getTitle());
            preparedStatement.setString(3, task.getDescription());
            preparedStatement.setString(4, task.getStatus().name());
            preparedStatement.setObject(5, null);
            preparedStatement.setObject(6, task.getReporter().getId());
            preparedStatement.setObject(7, projectId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    public Task getTaskById(UUID taskId, UUID projectId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_TASK_BY_ID_AND_PROJECT_ID)) {
            preparedStatement.setObject(1, taskId);
            preparedStatement.setObject(2, projectId);

            ResultSet resultSet = preparedStatement.executeQuery();

            return MapperUtil.getTask(resultSet);
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    public List<Task> getAllTasks(UUID projectId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_TASKS_BY_PROJECT_ID)) {
            preparedStatement.setObject(1, projectId);

            ResultSet resultSet = preparedStatement.executeQuery();

            return MapperUtil.getTaskList(resultSet);
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    public boolean deleteTask(UUID taskId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TASK)) {
            preparedStatement.setObject(1, taskId);

            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    public void updateTask(Task task) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TASK_BY_ID)) {
            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setString(3, task.getStatus().name());
            preparedStatement.setObject(4, task.getAssignee().getId());
            preparedStatement.setObject(5, task.getReporter().getId());
            preparedStatement.setObject(6, task.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

}
