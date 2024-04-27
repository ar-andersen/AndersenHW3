package com.rybak.andersenhw3.service;

import com.rybak.andersenhw3.dto.TaskUpdateDto;
import com.rybak.andersenhw3.entity.Project;
import com.rybak.andersenhw3.entity.Status;
import com.rybak.andersenhw3.entity.Task;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.TaskNotFoundException;
import com.rybak.andersenhw3.exception.UserNotFoundException;
import com.rybak.andersenhw3.storage.GlobalStorage;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class TaskService {

    private ProjectService projectService;
    private UserService userService;

    public TaskService(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
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

        GlobalStorage.tasks.add(task);
        project.addTask(task);

        return task;
    }

    public Task getTaskById(UUID projectId, UUID taskId) {
        Project project = projectService.getProjectById(projectId);

        return project.getTasks().stream()
                .filter(task -> task.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task with id %s not found", taskId)));
    }

    public List<Task> getAllTasksByProjectId(UUID projectId) {
        return GlobalStorage.projects.stream()
                .filter(project -> project.getId().equals(projectId))
                .map(Project::getTasks)
                .flatMap(Collection::stream)
                .toList();
    }

    public boolean deleteTaskById(UUID id) {
        return GlobalStorage.tasks.removeIf(task -> task.getId().equals(id));
    }

    public Task updateTask(UUID projectId, UUID taskId, TaskUpdateDto taskUpdateDto) {
        Task task = getTaskById(projectId, taskId);

        task.setDescription(taskUpdateDto.getDescription());
        task.setTitle(taskUpdateDto.getTitle());
        task.setStatus(taskUpdateDto.getStatus());
        task.setAssignee(userService.getUserById(UUID.fromString(taskUpdateDto.getAssigneeId())));
        task.setReporter(userService.getUserById(UUID.fromString(taskUpdateDto.getReporterId())));

        return task;
    }

}
