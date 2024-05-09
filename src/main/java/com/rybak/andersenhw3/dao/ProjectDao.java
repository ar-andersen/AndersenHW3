package com.rybak.andersenhw3.dao;

import com.rybak.andersenhw3.config.DataSource;
import com.rybak.andersenhw3.entity.Project;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.TaskManagerGlobalException;
import com.rybak.andersenhw3.util.MapperUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ProjectDao {

    private static final String INSERT_PROJECT = "INSERT INTO projects (id, name, description) VALUES (?,?,?)";
    private static final String ADD_USER_TO_PROJECT = "INSERT INTO projects_users (user_id, project_id) VALUES (?,?)";
    private static final String GET_PROJECT_BY_ID = "SELECT * FROM projects WHERE id = ?";
    private static final String DELETE_PROJECT_BY_ID = "DELETE FROM projects WHERE id = ?";
    private static final String GET_TEAM_BY_PROJECT_ID = "SELECT user_id as id, name, email, password, role FROM projects_users pu JOIN users u ON u.id = pu.user_id WHERE project_id = ?";
    private static final String GET_ALL_PROJECTS = "SELECT * FROM projects";
    private static final String DELETE_USER_FROM_PROJECT = "DELETE FROM projects_users WHERE project_id = ? AND user_id = ?";

    public void saveProject(Project project) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROJECT)) {
            preparedStatement.setObject(1, project.getId());
            preparedStatement.setString(2, project.getName());
            preparedStatement.setString(3, project.getDescription());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    public void addUserToProject(UUID userId, UUID projectId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER_TO_PROJECT)) {
            preparedStatement.setObject(1, userId);
            preparedStatement.setObject(2, projectId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    public Project findProjectById(UUID projectId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_PROJECT_BY_ID)) {
            preparedStatement.setObject(1, projectId);

            ResultSet resultSet = preparedStatement.executeQuery();

            return MapperUtil.getProject(resultSet);
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    public boolean deleteProjectById(UUID projectId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PROJECT_BY_ID)) {
            preparedStatement.setObject(1, projectId);

            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    public List<User> getProjectTeamByProjectId(UUID projectId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_TEAM_BY_PROJECT_ID)) {
            preparedStatement.setObject(1, projectId);

            ResultSet resultSet = preparedStatement.executeQuery();

            return MapperUtil.getUserList(resultSet);
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    public List<Project> getAllProjects() {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_PROJECTS)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Project> projects = MapperUtil.getProjectList(resultSet);
            for (Project project : projects) {
                List<User> team = getProjectTeamByProjectId(project.getId());
                project.setTeam(team);
            }

            return projects;
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    public void deleteUserFromProject(UUID projectId, UUID userId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_FROM_PROJECT)) {
            preparedStatement.setObject(1, projectId);
            preparedStatement.setObject(2, userId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

}
