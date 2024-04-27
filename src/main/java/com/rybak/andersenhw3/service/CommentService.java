package com.rybak.andersenhw3.service;

import com.rybak.andersenhw3.entity.Comment;
import com.rybak.andersenhw3.entity.Task;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.ProjectNotFoundException;
import com.rybak.andersenhw3.exception.UserNotFoundException;
import com.rybak.andersenhw3.storage.GlobalStorage;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class CommentService {

    private TaskService taskService;
    private UserService userService;

    public CommentService(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    public Comment createComment(UUID projectId, UUID taskId, Comment comment, UUID userId) {
        Task taskFromDb = taskService.getTaskById(projectId, taskId);

        boolean noUserInTeam = GlobalStorage.projects.stream()
                .filter(project -> project.getId().equals(projectId))
                .findFirst()
                .orElseThrow(() -> new ProjectNotFoundException(String.format("No project with id '%s'", projectId)))
                .getTeam().stream()
                .map(User::getId)
                .noneMatch(teamMemberId -> teamMemberId.equals(userId));

        if (noUserInTeam) {
            throw new UserNotFoundException(String.format("No user '%s' in team of project '%s'", userId, projectId));
        }

        comment.setId(UUID.randomUUID());
        comment.setUser(userService.getUserById(userId));

        GlobalStorage.comments.add(comment);
        taskFromDb.addComment(comment);

        return comment;
    }

    public List<Comment> getAllCommentsByTask(UUID projectId, UUID taskId) {
        return taskService.getAllTasksByProjectId(projectId).stream()
                .filter(task -> task.getId().equals(taskId))
                .map(Task::getComments)
                .flatMap(Collection::stream)
                .toList();
    }

}
