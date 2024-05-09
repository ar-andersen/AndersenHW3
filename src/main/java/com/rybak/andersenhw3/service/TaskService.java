package com.rybak.andersenhw3.service;

import com.rybak.andersenhw3.dao.TaskDao;
import com.rybak.andersenhw3.dto.TaskUpdateDto;
import com.rybak.andersenhw3.entity.Project;
import com.rybak.andersenhw3.entity.Status;
import com.rybak.andersenhw3.entity.Task;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.TaskNotFoundException;
import com.rybak.andersenhw3.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;

public class TaskService {

    private final ProjectService projectService;
    private final UserService userService;
    private final TaskDao taskDao;

    public TaskService(ProjectService projectService, UserService userService, TaskDao taskDao) {
        this.projectService = projectService;
        this.userService = userService;
        this.taskDao = taskDao;
    }

    public Task createTask(UUID projectId, Task task, UUID reporterId) {
        Project project = projectService.getProjectById(projectId);
        User reporter = userService.getUserById(reporterId);

        boolean noUserInTeam = project.getTeam().stream()
                .map(User::getId)
                .noneMatch(teamMemberId -> teamMemberId.equals(reporterId));

        if (noUserInTeam) {
            throw new UserNotFoundException(String.format("No user '%s' in team of project '%s'", reporterId, projectId));
        }

        task.setStatus(Status.TO_DO);
        task.setReporter(reporter);
        task.setId(UUID.randomUUID());

        taskDao.saveTask(task, projectId);

        return task;
    }

    public Task getTaskById(UUID projectId, UUID taskId) {
        projectService.getProjectById(projectId);

        Task task = taskDao.getTaskById(taskId, projectId);

        if (task == null) {
            throw new TaskNotFoundException(String.format("Task with id '%s' not found", taskId));
        }

        if (task.getReporter().getId() != null) {
            task.setReporter(userService.getUserById(task.getReporter().getId()));
        }
        if (task.getAssignee().getId() != null) {
            task.setAssignee(userService.getUserById(task.getAssignee().getId()));
        }

        return task;
    }

    public List<Task> getAllTasksByProjectId(UUID projectId) {
        return taskDao.getAllTasks(projectId);
    }

    public boolean deleteTaskById(UUID id) {
        return taskDao.deleteTask(id);
    }

    public Task updateTask(UUID projectId, UUID taskId, TaskUpdateDto taskUpdateDto) {
        Task task = getTaskById(projectId, taskId);

        task.setDescription(taskUpdateDto.getDescription());
        task.setTitle(taskUpdateDto.getTitle());
        task.setStatus(taskUpdateDto.getStatus());
        task.setAssignee(userService.getUserById(UUID.fromString(taskUpdateDto.getAssigneeId())));
        task.setReporter(userService.getUserById(UUID.fromString(taskUpdateDto.getReporterId())));

        taskDao.updateTask(task);

        return task;
    }

}
