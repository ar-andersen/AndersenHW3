package com.rybak.andersenhw3.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybak.andersenhw3.dao.ProjectDao;
import com.rybak.andersenhw3.dao.TaskDao;
import com.rybak.andersenhw3.dao.UserDao;
import com.rybak.andersenhw3.dto.ProjectCreateDto;
import com.rybak.andersenhw3.dto.ProjectResponseDto;
import com.rybak.andersenhw3.dto.ProjectTeamUpdateDto;
import com.rybak.andersenhw3.dto.TaskCreateDto;
import com.rybak.andersenhw3.dto.TaskUpdateDto;
import com.rybak.andersenhw3.entity.Project;
import com.rybak.andersenhw3.entity.Task;
import com.rybak.andersenhw3.exception.TaskManagerGlobalException;
import com.rybak.andersenhw3.mapper.CommentMapper;
import com.rybak.andersenhw3.mapper.ProjectMapper;
import com.rybak.andersenhw3.mapper.TaskMapper;
import com.rybak.andersenhw3.mapper.UserMapper;
import com.rybak.andersenhw3.service.ProjectService;
import com.rybak.andersenhw3.service.TaskService;
import com.rybak.andersenhw3.service.UserService;
import com.rybak.andersenhw3.util.JsonUtil;
import com.rybak.andersenhw3.util.RegexUtil;
import com.rybak.andersenhw3.util.WebUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/projects/*")
public class ProjectServlet extends HttpServlet {

    private ProjectService projectService;
    private ProjectMapper projectMapper;
    private ObjectMapper objectMapper;
    private TaskService taskService;
    private TaskMapper taskMapper;
    private UserService userService;
    private UserMapper userMapper;
    private CommentMapper commentMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        objectMapper = new ObjectMapper();
        userService = new UserService(new UserDao());
        userMapper = new UserMapper();
        projectService = new ProjectService(userService, new ProjectDao());
        commentMapper = new CommentMapper(userMapper);
        taskService = new TaskService(projectService, userService, new TaskDao());
        taskMapper = new TaskMapper(commentMapper);
        projectMapper = new ProjectMapper(userMapper, taskMapper);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = WebUtil.getResolvedPathInfo(request);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Pattern pattern = RegexUtil.UUID_PATTERN;
        Matcher matcher = pattern.matcher(pathInfo);

        Pattern taskPattern = RegexUtil.TASKS_PATTERN;
        Matcher taskMatcher = taskPattern.matcher(pathInfo);

        if (taskMatcher.matches()) {
            String projectId = taskMatcher.group(1);

            try {
                List<Task> tasks = taskService.getAllTasksByProjectId(UUID.fromString(projectId));
                out.print(objectMapper.writeValueAsString(taskMapper.toTaskResponseDtoList(tasks)));
                response.setStatus(200);
            } catch (TaskManagerGlobalException e) {
                WebUtil.sendFailMessage(response, e.getMessage(), e.getCode());
            }
        } else if (matcher.matches()) {
            try {
                String id = matcher.group(1);
                Project project = projectService.getProjectById(UUID.fromString(id));

                out.print(objectMapper.writeValueAsString(projectMapper.toProjectResponseDto(project)));
                response.setStatus(200);
            } catch (TaskManagerGlobalException e) {
                WebUtil.sendFailMessage(response, e.getMessage(), e.getCode());
            }
        } else if (pathInfo.equals("/")) {
            try {
                List<ProjectResponseDto> projects = projectService.getAllProjects().stream()
                        .map(projectMapper::toProjectResponseDto)
                        .toList();

                out.print(objectMapper.writeValueAsString(projects));
                response.setStatus(200);
            } catch (TaskManagerGlobalException e) {
                WebUtil.sendFailMessage(response, e.getMessage(), e.getCode());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = WebUtil.getResolvedPathInfo(request);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Matcher taskMatcher = RegexUtil.TASKS_PATTERN.matcher(pathInfo);
        Matcher projectUserMatcher = RegexUtil.USERS_PATTERN.matcher(pathInfo);

        if (projectUserMatcher.matches()) {
            String projectId = projectUserMatcher.group(1);

            String json = JsonUtil.getBody(request);
            ProjectTeamUpdateDto teamUpdateDto = objectMapper.readValue(json, ProjectTeamUpdateDto.class);

            try {
                Project project = projectService.addUserToTeam(UUID.fromString(projectId),
                        UUID.fromString(teamUpdateDto.getUserId()));

                out.print(objectMapper.writeValueAsString(projectMapper.toProjectResponseDto(project)));
                response.setStatus(200);
            } catch (TaskManagerGlobalException e) {
                WebUtil.sendFailMessage(response, e.getMessage(), e.getCode());
            }
        } else if (taskMatcher.matches()) {
            String projectId = taskMatcher.group(1);

            String json = JsonUtil.getBody(request);
            TaskCreateDto taskCreateDto = objectMapper.readValue(json, TaskCreateDto.class);

            try {
                Task task = taskService.createTask(UUID.fromString(projectId), taskMapper.toTask(taskCreateDto),
                        UUID.fromString(taskCreateDto.getReporterId()));

                out.print(objectMapper.writeValueAsString(taskMapper.toTaskResponseDto(task)));
                response.setStatus(201);
            } catch (TaskManagerGlobalException e) {
                WebUtil.sendFailMessage(response, e.getMessage(), e.getCode());
            }
        } else if (pathInfo.equals("/")) {
            String json = JsonUtil.getBody(request);
            try {
                Project project = projectService.createProject(projectMapper.toProject(
                        objectMapper.readValue(json, ProjectCreateDto.class)), WebUtil.getCurrentUserEmail(request));

                out.print(objectMapper.writeValueAsString(project));
                response.setStatus(201);
            } catch (TaskManagerGlobalException e) {
                WebUtil.sendFailMessage(response, e.getMessage(), e.getCode());
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = WebUtil.getResolvedPathInfo(request);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Pattern pattern = RegexUtil.TASKS_WITH_ID_PATTERN;
        Matcher matcher = pattern.matcher(pathInfo);

        if (matcher.matches()) {
            String projectId = matcher.group(1);
            String taskId = matcher.group(2);

            String json = JsonUtil.getBody(request);
            TaskUpdateDto taskUpdateDto = objectMapper.readValue(json, TaskUpdateDto.class);

            try {
                Task task = taskService.updateTask(UUID.fromString(projectId), UUID.fromString(taskId), taskUpdateDto);

                out.print(objectMapper.writeValueAsString(taskMapper.toTaskResponseDto(task)));
                response.setStatus(200);
            } catch (TaskManagerGlobalException e) {
                WebUtil.sendFailMessage(response, e.getMessage(), e.getCode());
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = WebUtil.getResolvedPathInfo(request);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Matcher projectMatcher = RegexUtil.UUID_PATTERN.matcher(pathInfo);
        Matcher projectUserMatcher = RegexUtil.USERS_PATTERN.matcher(pathInfo);

        if (projectUserMatcher.matches()) {
            String projectId = projectUserMatcher.group(1);

            String json = JsonUtil.getBody(request);
            ProjectTeamUpdateDto teamUpdateDto = objectMapper.readValue(json, ProjectTeamUpdateDto.class);

            try {
                Project project = projectService.removeUserFromTeam(UUID.fromString(projectId),
                        UUID.fromString(teamUpdateDto.getUserId()));

                out.print(objectMapper.writeValueAsString(projectMapper.toProjectResponseDto(project)));
                response.setStatus(200);
            } catch (TaskManagerGlobalException e) {
                WebUtil.sendFailMessage(response, e.getMessage(), e.getCode());
            }
        } else if (projectMatcher.matches()) {
            String id = projectMatcher.group(1);
            boolean projectDeleted = projectService.deleteProjectById(UUID.fromString(id));
            if (projectDeleted) {
                response.setStatus(204);
            } else {
                response.setStatus(404);
                String message = String.format("Project with id %s not found", id);
                WebUtil.sendFailMessage(response, message, 404);
            }
        }
    }

}
