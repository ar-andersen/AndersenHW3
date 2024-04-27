package com.rybak.andersenhw3.service;

import com.rybak.andersenhw3.dto.TaskUpdateDto;
import com.rybak.andersenhw3.entity.Project;
import com.rybak.andersenhw3.entity.Status;
import com.rybak.andersenhw3.entity.Task;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.ProjectNotFoundException;
import com.rybak.andersenhw3.exception.TaskNotFoundException;
import com.rybak.andersenhw3.exception.UserNotFoundException;
import com.rybak.andersenhw3.storage.GlobalStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    private final String NAME = "simple project";
    private final String DESCRIPTION = "simple project description";
    private final String EMAIL = "alex@gmail.com";
    private final String TITLE = "task title";
    private final String TASK_DESCRIPTION = "task description";
    private User user;
    private Project project;
    private Task task;

    @Mock
    private UserService userService;
    @Mock
    private ProjectService projectService;
    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setEmail(EMAIL);

        project = new Project(null, NAME, DESCRIPTION, new ArrayList<>(), new ArrayList<>());

        task = new Task(null, TITLE, TASK_DESCRIPTION, Status.TO_DO, null, null, new ArrayList<>());
    }

    @AfterEach
    public void teardown() {
        GlobalStorage.tasks.clear();
        GlobalStorage.users.clear();
        GlobalStorage.projects.clear();
    }

    @Test
    void createTask_WhenProjectNotExist_ShouldThrowProjectNotFoundException() {
        UUID projectId = UUID.randomUUID();
        UUID reporterId = UUID.randomUUID();
        Mockito.when(projectService.getProjectById(projectId)).thenThrow(ProjectNotFoundException.class);

        Assertions.assertThrows(ProjectNotFoundException.class, () -> taskService.createTask(projectId, task, reporterId));
    }

    @Test
    void createTask_WhenReporterNotExist_ShouldThrowProjectNotFoundException() {
        UUID projectId = UUID.randomUUID();
        UUID reporterId = UUID.randomUUID();
        Mockito.when(projectService.getProjectById(projectId)).thenReturn(project);
        Mockito.when(userService.getUserById(reporterId)).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, () -> taskService.createTask(projectId, task, reporterId));
    }

    @Test
    void createTask_WhenReporterIsNotInTeam_ShouldThrowUserNotFoundException() {
        UUID projectId = UUID.randomUUID();
        UUID reporterId = UUID.randomUUID();
        Mockito.when(projectService.getProjectById(projectId)).thenReturn(project);
        Mockito.when(userService.getUserById(reporterId)).thenReturn(user);

        Assertions.assertThrows(UserNotFoundException.class, () -> taskService.createTask(projectId, task, reporterId));
    }

    @Test
    void createTask_WhenReporterIsInTeam_ShouldCreateTask() {
        UUID projectId = UUID.randomUUID();
        UUID reporterId = UUID.randomUUID();
        user.setId(reporterId);
        project.addUser(user);
        Mockito.when(projectService.getProjectById(projectId)).thenReturn(project);
        Mockito.when(userService.getUserById(reporterId)).thenReturn(user);

        Task actual = taskService.createTask(projectId, task, reporterId);

        Assertions.assertEquals(Status.TO_DO, actual.getStatus());
        Assertions.assertEquals(task.getDescription(), actual.getDescription());
        Assertions.assertEquals(task.getComments(), actual.getComments());
        Assertions.assertEquals(task.getReporter(), user);
    }

    @Test
    void getTaskById_WhenProjectNotExist_ShouldThrowProjectNotFoundException() {
        UUID projectId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        Mockito.when(projectService.getProjectById(projectId)).thenThrow(ProjectNotFoundException.class);

        Assertions.assertThrows(ProjectNotFoundException.class, () -> taskService.getTaskById(projectId, taskId));
    }

    @Test
    void getTaskById_WhenProjectAndTaskExist_ShouldReturnTask() {
        UUID projectId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        task.setId(taskId);
        project.addTask(task);
        Mockito.when(projectService.getProjectById(projectId)).thenReturn(project);

        Task actual = taskService.getTaskById(projectId, taskId);

        Assertions.assertEquals(task, actual);
    }

    @Test
    void getTaskById_WhenTaskNotExist_ShouldThrowTaskNotFoundException() {
        UUID projectId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        Mockito.when(projectService.getProjectById(projectId)).thenReturn(project);

        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(projectId, taskId));
    }

    @Test
    void getAllTasksByProjectId_ShouldReturnAllTasks() {
        UUID projectId = UUID.randomUUID();
        project.setId(projectId);
        project.addTask(task);
        GlobalStorage.projects.add(project);

        List<Task> tasks = taskService.getAllTasksByProjectId(projectId);

        Assertions.assertEquals(1, tasks.size());
    }

    @Test
    void deleteTaskById_WhenTaskExist_ShouldReturnTrue() {
        UUID taskId = UUID.randomUUID();
        task.setId(taskId);
        GlobalStorage.tasks.add(task);

        boolean actual = taskService.deleteTaskById(taskId);

        Assertions.assertTrue(actual);
    }

    @Test
    void deleteTaskById_WhenTaskNotExist_ShouldReturnFalse() {
        UUID taskId = UUID.randomUUID();
        task.setId(taskId);
        GlobalStorage.tasks.add(task);

        boolean actual = taskService.deleteTaskById(UUID.randomUUID());

        Assertions.assertFalse(actual);
    }

    @Test
    void updateTask_WhenReporterIsNotExist_ShouldThrowUserNotFoundException() {
        UUID projectId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        String assigneeId = "884509de-47c8-4883-a9f8-8541370134ed";
        String reporterId = "9647e4cd-9aee-4cd0-9a45-b33f7c6dc9ce";
        String newTitle = "new title";
        task.setId(taskId);
        project.addTask(task);
        TaskUpdateDto taskUpdateDto = new TaskUpdateDto(newTitle, DESCRIPTION, Status.IN_TESTING, assigneeId, reporterId);
        Mockito.when(userService.getUserById(UUID.fromString(reporterId))).thenThrow(UserNotFoundException.class);
        Mockito.when(userService.getUserById(UUID.fromString(assigneeId))).thenReturn(user);
        Mockito.when(projectService.getProjectById(projectId)).thenReturn(project);

        Assertions.assertThrows(UserNotFoundException.class, () -> taskService.updateTask(projectId, taskId, taskUpdateDto));
    }

    @Test
    void updateTask_WhenAllUsersExist_ShouldReturnTask() {
        UUID projectId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        String assigneeId = "884509de-47c8-4883-a9f8-8541370134ed";
        String reporterId = "9647e4cd-9aee-4cd0-9a45-b33f7c6dc9ce";
        String newTitle = "new title";
        task.setId(taskId);
        project.addTask(task);
        TaskUpdateDto taskUpdateDto = new TaskUpdateDto(newTitle, DESCRIPTION, Status.IN_TESTING, assigneeId, reporterId);
        Mockito.when(userService.getUserById(UUID.fromString(reporterId))).thenReturn(user);
        Mockito.when(userService.getUserById(UUID.fromString(assigneeId))).thenReturn(user);
        Mockito.when(projectService.getProjectById(projectId)).thenReturn(project);

        Task actual = taskService.updateTask(projectId, taskId, taskUpdateDto);

        Assertions.assertEquals(task, actual);
    }

}