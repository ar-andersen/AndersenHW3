package com.rybak.andersenhw3.dao;

import com.rybak.andersenhw3.config.DataSource;
import com.rybak.andersenhw3.entity.Comment;
import com.rybak.andersenhw3.exception.TaskManagerGlobalException;
import com.rybak.andersenhw3.util.MapperUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class CommentDao {

    private static final String CREATE_COMMENT = "INSERT INTO comments (id, title, user_id, task_id) VALUES (?,?,?,?)";
    private static final String GET_ALL_COMMENTS_BY_TASK_ID = "SELECT * FROM comments WHERE task_id = ?";

    public void saveComment(Comment comment, UUID taskId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_COMMENT)) {
            preparedStatement.setObject(1, comment.getId());
            preparedStatement.setString(2, comment.getMessage());
            preparedStatement.setObject(3, comment.getUser().getId());
            preparedStatement.setObject(4, taskId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

    public List<Comment> getAllCommentsByTaskId(UUID taskId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_COMMENTS_BY_TASK_ID)) {
            preparedStatement.setObject(1, taskId);

            ResultSet resultSet = preparedStatement.executeQuery();

            return MapperUtil.getCommentList(resultSet);
        } catch (SQLException e) {
            throw new TaskManagerGlobalException(e.getMessage(), 500);
        }
    }

}
