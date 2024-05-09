package com.rybak.andersenhw3.filter;

import com.rybak.andersenhw3.dao.UserDao;
import com.rybak.andersenhw3.entity.Role;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.AccessDeniedException;
import com.rybak.andersenhw3.service.PermissionService;
import com.rybak.andersenhw3.service.UserService;
import com.rybak.andersenhw3.util.RegexUtil;
import com.rybak.andersenhw3.util.WebUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    private final UserDao userDao = new UserDao();
    private final UserService userService = new UserService(userDao);
    private final PermissionService permissionService = new PermissionService();
    private static final List<String> WHITE_LIST = List.of("/auth/login", "/auth/register");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);
        String requestURI = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        if (WHITE_LIST.contains(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        if (session != null && session.getAttribute("email") != null) {
            String email = (String) session.getAttribute("email");
            User user = userService.getUserByEmail(email);

            if (requestURI.equals("/projects") && Role.DM != user.getRole()
                    && (method.equals("POST") || method.equals("PUT"))) {
                WebUtil.sendFailMessage(response, "Access Denied", 403);
                return;
            }

            Matcher projectMatcher = RegexUtil.PROJECT_PATTERN.matcher(requestURI);
            Matcher projectTaskMatcher = RegexUtil.PROJECT_WITH_ID_AND_TASK_PATTERN.matcher(requestURI);
            Matcher taskCommentMatcher = RegexUtil.TASK_WITH_ID_AND_COMMENT_PATTERN.matcher(requestURI);
            Matcher projectUserMatcher = RegexUtil.PROJECT_WITH_ID_AND_USER_PATTERN.matcher(requestURI);

            if (projectMatcher.matches()) {
                String projectId = projectMatcher.group(1);
                try {
                    permissionService.checkAccessToProject(UUID.fromString(projectId), email);
                } catch (AccessDeniedException e) {
                    WebUtil.sendFailMessage(response, e.getMessage(), 403);
                    return;
                }
            }
            if (projectTaskMatcher.matches()) {
                String projectId = projectTaskMatcher.group(1);
                try {
                    permissionService.checkAccessToProject(UUID.fromString(projectId), email);
                } catch (AccessDeniedException e) {
                    WebUtil.sendFailMessage(response, e.getMessage(), 403);
                    return;
                }
            }
            if (taskCommentMatcher.matches()) {
                String projectId = httpRequest.getParameter("projectId");
                try {
                    permissionService.checkAccessToProject(UUID.fromString(projectId), email);
                } catch (AccessDeniedException e) {
                    WebUtil.sendFailMessage(response, e.getMessage(), 403);
                    return;
                }
            }
            if (projectUserMatcher.matches()) {
                String projectId = projectUserMatcher.group(1);
                try {
                    permissionService.checkAccessToProject(UUID.fromString(projectId), email);
                } catch (AccessDeniedException e) {
                    WebUtil.sendFailMessage(response, e.getMessage(), 403);
                    return;
                }
            }

            chain.doFilter(request, response);
        } else {
            WebUtil.sendFailMessage(response, "Unauthorized", 401);
        }
    }

}