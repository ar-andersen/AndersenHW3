package com.rybak.andersenhw3.service;

import com.rybak.andersenhw3.entity.Project;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.AccessDeniedException;
import com.rybak.andersenhw3.storage.GlobalStorage;

import java.util.Collection;
import java.util.UUID;

public class PermissionService {

    public void checkAccessToProject(UUID projectId, String email) {
        GlobalStorage.projects.stream()
                .filter(project -> project.getId().equals(projectId))
                .map(Project::getTeam)
                .flatMap(Collection::stream)
                .map(User::getEmail)
                .filter(userEmail -> userEmail.equals(email))
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException(
                        String.format("User with email '%s' has no access to project with id '%s'", email, projectId)));
    }

}
