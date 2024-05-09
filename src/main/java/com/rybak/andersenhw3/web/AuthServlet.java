package com.rybak.andersenhw3.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rybak.andersenhw3.dao.UserDao;
import com.rybak.andersenhw3.dto.LoginResponseDto;
import com.rybak.andersenhw3.dto.UserLoginDto;
import com.rybak.andersenhw3.dto.UserRegisterDto;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.TaskManagerGlobalException;
import com.rybak.andersenhw3.mapper.UserMapper;
import com.rybak.andersenhw3.service.AuthService;
import com.rybak.andersenhw3.util.JsonUtil;
import com.rybak.andersenhw3.util.WebUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {

    private AuthService authService;
    private ObjectMapper objectMapper;
    private UserMapper userMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        UserDao userDao = new UserDao();
        authService = new AuthService(userDao);
        objectMapper = new ObjectMapper();
        userMapper = new UserMapper();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = WebUtil.getResolvedPathInfo(request);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        if (pathInfo.equals("/register")) {
            String json = JsonUtil.getBody(request);
            User user = userMapper.toUser(objectMapper.readValue(json, UserRegisterDto.class));

            try {
                User registeredUser = authService.register(user);
                out.print(objectMapper.writeValueAsString(userMapper.toUserResponseDto(registeredUser)));
                response.setStatus(201);
            } catch (TaskManagerGlobalException e) {
                WebUtil.sendFailMessage(response, e.getMessage(), e.getCode());
            }
        } else if (pathInfo.equals("/login")) {
            String json = JsonUtil.getBody(request);
            UserLoginDto userLoginDto = objectMapper.readValue(json, UserLoginDto.class);

            try {
                boolean successfulLogin = authService.login(userLoginDto.getEmail(), userLoginDto.getPassword());
                if (successfulLogin) {
                    HttpSession session = request.getSession();
                    session.setAttribute("email", userLoginDto.getEmail());

                    out.print(objectMapper.writeValueAsString(new LoginResponseDto(true)));
                    response.setStatus(200);
                }
            } catch (TaskManagerGlobalException e) {
                WebUtil.sendFailMessage(response, e.getMessage(), e.getCode());
            }
        }
    }

}
