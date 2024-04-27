package com.rybak.andersenhw3.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybak.andersenhw3.dto.CommentCreateDto;
import com.rybak.andersenhw3.entity.Comment;
import com.rybak.andersenhw3.exception.TaskManagerGlobalException;
import com.rybak.andersenhw3.mapper.CommentMapper;
import com.rybak.andersenhw3.mapper.UserMapper;
import com.rybak.andersenhw3.service.CommentService;
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

@WebServlet("/tasks/*")
public class TaskServlet extends HttpServlet {

    private TaskService taskService;
    private CommentService commentService;
    private ObjectMapper objectMapper;
    private UserService userService;
    private CommentMapper commentMapper;
    private UserMapper userMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        objectMapper = new ObjectMapper();
        userService = new UserService();
        taskService = new TaskService(new ProjectService(userService), userService);
        commentService = new CommentService(taskService, userService);
        userMapper = new UserMapper();
        commentMapper = new CommentMapper(userMapper);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = WebUtil.getResolvedPathInfo(request);
        String projectId = request.getParameter("projectId");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Pattern pattern = RegexUtil.COMMENTS_PATTERN;
        Matcher matcher = pattern.matcher(pathInfo);

        if (matcher.matches()) {
            String taskId = matcher.group(1);

            String json = JsonUtil.getBody(request);
            CommentCreateDto commentCreateDto = objectMapper.readValue(json, CommentCreateDto.class);

            try {
                Comment comment = commentService.createComment(UUID.fromString(projectId), UUID.fromString(taskId),
                        commentMapper.toComment(commentCreateDto), UUID.fromString(commentCreateDto.getUserId()));
                out.print(objectMapper.writeValueAsString(comment));
                response.setStatus(200);
            } catch (TaskManagerGlobalException e) {
                WebUtil.sendFailMessage(response, e.getMessage(), e.getCode());
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = WebUtil.getResolvedPathInfo(request);
        String projectId = request.getParameter("projectId");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Pattern pattern = RegexUtil.COMMENTS_PATTERN;
        Matcher matcher = pattern.matcher(pathInfo);

        if (matcher.matches()) {
            String taskId = matcher.group(1);

            try {
                List<Comment> comments = commentService.getAllCommentsByTask(UUID.fromString(projectId), UUID.fromString(taskId));
                out.print(objectMapper.writeValueAsString(commentMapper.toCommentResponseDtoList(comments)));
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

        Pattern pattern = RegexUtil.UUID_PATTERN;
        Matcher matcher = pattern.matcher(pathInfo);

        if (matcher.matches()) {
            String id = matcher.group(1);
            boolean taskDeleted = taskService.deleteTaskById(UUID.fromString(id));
            if (taskDeleted) {
                response.setStatus(204);
            } else {
                response.setStatus(404);
                String message = String.format("Task with id %s not found", id);
                WebUtil.sendFailMessage(response, message, 404);
            }
        }
    }

}
