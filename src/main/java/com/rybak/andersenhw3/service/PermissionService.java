package com.rybak.andersenhw3.service;

import com.rybak.andersenhw3.dao.ProjectDao;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.AccessDeniedException;

import java.util.UUID;

public class PermissionService {

    private final ProjectDao projectDao = new ProjectDao();

    public void checkAccessToProject(UUID projectId, String email) {
        projectDao.getProjectTeamByProjectId(projectId).stream()
                .map(User::getEmail)
                .filter(userEmail -> userEmail.equals(email))
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException(
                        String.format("User with email '%s' has no access to project with id '%s'", email, projectId)));
    }

}
