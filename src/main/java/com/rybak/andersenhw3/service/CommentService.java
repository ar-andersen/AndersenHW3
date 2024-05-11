package com.rybak.andersenhw3.service;

import com.rybak.andersenhw3.dao.CommentDao;
import com.rybak.andersenhw3.entity.Comment;
import com.rybak.andersenhw3.entity.Task;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;

public class CommentService {

    private final TaskService taskService;
    private final UserService userService;
    private final CommentDao commentDao;
    private final ProjectService projectService;

    public CommentService(TaskService taskService,
                          UserService userService,
                          CommentDao commentDao,
                          ProjectService projectService) {
        this.taskService = taskService;
        this.userService = userService;
        this.commentDao = commentDao;
        this.projectService = projectService;
    }

    public Comment createComment(UUID projectId, UUID taskId, Comment comment, UUID userId) {
        Task task = taskService.getTaskById(projectId, taskId);

        boolean noUserInTeam = projectService.getProjectById(projectId).getTeam().stream()
                .map(User::getId)
                .noneMatch(teamMemberId -> teamMemberId.equals(userId));

        if (noUserInTeam) {
            throw new UserNotFoundException(String.format("No user '%s' in team of project '%s'", userId, projectId));
        }

        comment.setId(UUID.randomUUID());
        comment.setUser(userService.getUserById(userId));
        comment.setTask(task);

        commentDao.saveComment(comment);

        return comment;
    }

    public List<Comment> getAllCommentsByTask(UUID taskId) {
        return commentDao.getAllCommentsByTaskId(taskId);
    }

}
