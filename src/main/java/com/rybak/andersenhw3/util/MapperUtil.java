package com.rybak.andersenhw3.util;

import com.rybak.andersenhw3.entity.Comment;
import com.rybak.andersenhw3.entity.Project;
import com.rybak.andersenhw3.entity.Role;
import com.rybak.andersenhw3.entity.Status;
import com.rybak.andersenhw3.entity.Task;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.TaskManagerGlobalException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MapperUtil {

    private MapperUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static User getUser(ResultSet resultSet) {
        try {
            if (!resultSet.next()) {
                return null;
            }

            return mapToUser(resultSet);
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    public static List<User> getUserList(ResultSet resultSet) {
        try {
            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                User user = mapToUser(resultSet);
                users.add(user);
            }

            return users;
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    private static User mapToUser(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setId(resultSet.getObject("id", UUID.class));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        user.setRole(Role.valueOf(resultSet.getString("role")));

        return user;
    }

    public static Project getProject(ResultSet resultSet) {
        try {
            if (!resultSet.next()) {
                return null;
            }

            return mapToProject(resultSet);
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    private static Project mapToProject(ResultSet resultSet) throws SQLException {
        Project project = new Project();

        project.setId(resultSet.getObject("id", UUID.class));
        project.setName(resultSet.getString("name"));
        project.setDescription(resultSet.getString("description"));

        return project;
    }

    public static List<Project> getProjectList(ResultSet resultSet) {
        try {
            List<Project> projects = new ArrayList<>();

            while (resultSet.next()) {
                Project project = mapToProject(resultSet);
                projects.add(project);
            }

            return projects;
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    public static Task getTask(ResultSet resultSet) {
        try {
            if (!resultSet.next()) {
                return null;
            }

            return mapToTask(resultSet);
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    private static Task mapToTask(ResultSet resultSet) throws SQLException {
        Task task = new Task();

        task.setId(resultSet.getObject("id", UUID.class));
        task.setTitle(resultSet.getString("title"));
        task.setDescription(resultSet.getString("description"));
        task.setStatus(Status.valueOf(resultSet.getString("status")));

        User assignee = new User();
        assignee.setId(resultSet.getObject("assignee_id", UUID.class));
        task.setAssignee(assignee);
        User reporter = new User();
        reporter.setId(resultSet.getObject("reporter_id", UUID.class));
        task.setReporter(reporter);

        return task;
    }

    public static List<Task> getTaskList(ResultSet resultSet) {
        try {
            List<Task> tasks = new ArrayList<>();

            while (resultSet.next()) {
                Task task = mapToTask(resultSet);
                tasks.add(task);
            }

            return tasks;
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    private static Comment mapToComment(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();

        comment.setId(resultSet.getObject("id", UUID.class));
        comment.setMessage(resultSet.getString("title"));

        User user = new User();
        user.setId(resultSet.getObject("user_id", UUID.class));
        comment.setUser(user);

        return comment;
    }

    public static List<Comment> getCommentList(ResultSet resultSet) {
        try {
            List<Comment> comments = new ArrayList<>();

            while (resultSet.next()) {
                Comment comment = mapToComment(resultSet);
                comments.add(comment);
            }

            return comments;
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

}
