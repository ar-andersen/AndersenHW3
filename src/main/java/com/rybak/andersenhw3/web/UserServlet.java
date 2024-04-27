package com.rybak.andersenhw3.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybak.andersenhw3.dto.UserResponseDto;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.TaskManagerGlobalException;
import com.rybak.andersenhw3.mapper.UserMapper;
import com.rybak.andersenhw3.service.UserService;
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

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {

    private UserService userService;
    private ObjectMapper objectMapper;
    private UserMapper userMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userService = new UserService();
        objectMapper = new ObjectMapper();
        userMapper = new UserMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = WebUtil.getResolvedPathInfo(request);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Pattern pattern = RegexUtil.UUID_PATTERN;
        Matcher matcher = pattern.matcher(pathInfo);

        if (matcher.matches()) {
            try {
                String id = matcher.group(1);
                User userById = userService.getUserById(UUID.fromString(id));

                out.print(objectMapper.writeValueAsString(userMapper.toUserResponseDto(userById)));
                response.setStatus(200);
            } catch (TaskManagerGlobalException e) {
                WebUtil.sendFailMessage(response, e.getMessage(), e.getCode());
            }
        } else if (pathInfo.equals("/")) {
            try {
                List<UserResponseDto> users = userService.getAllUsers().stream()
                        .map(userMapper::toUserResponseDto)
                        .toList();

                out.print(objectMapper.writeValueAsString(users));
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
            boolean userDeleted = userService.deleteUserById(UUID.fromString(id));
            if (userDeleted) {
                response.setStatus(204);
            } else {
                response.setStatus(404);
                String message = String.format("User with id %s not found", id);
                WebUtil.sendFailMessage(response, message, 404);
            }
        }
    }

}
