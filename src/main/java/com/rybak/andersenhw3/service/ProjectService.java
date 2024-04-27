package com.rybak.andersenhw3.service;

import com.rybak.andersenhw3.entity.Project;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.ProjectNotFoundException;
import com.rybak.andersenhw3.exception.UserNotFoundException;
import com.rybak.andersenhw3.storage.GlobalStorage;

import java.util.List;
import java.util.UUID;

public class ProjectService {

    private final UserService userService;

    public ProjectService(UserService userService) {
        this.userService = userService;
    }

    public Project createProject(Project project, String email) {
        User user = userService.getUserByEmail(email);

        List<Project> projects = GlobalStorage.projects;

        project.setId(UUID.randomUUID());
        project.addUser(user);
        projects.add(project);

        return project;
    }

    public Project getProjectById(UUID id) {
        return GlobalStorage.projects.stream()
                .filter(project -> project.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ProjectNotFoundException(String.format("Project with id %s not found", id)));
    }

    public boolean deleteProjectById(UUID id) {
        return GlobalStorage.projects.removeIf(project -> project.getId().equals(id));
    }

    public List<Project> getAllProjects() {
        return List.copyOf(GlobalStorage.projects);
    }

    public Project addUserToTeam(UUID projectId, UUID userId) {
        Project project = getProjectById(projectId);

        boolean noUserInProject = project.getTeam().stream()
                .map(User::getId)
                .noneMatch(teamMemberId -> teamMemberId.equals(userId));

        if (noUserInProject) {
            User user = userService.getUserById(userId);
            project.addUser(user);
        }

        return project;
    }

    public Project removeUserFromTeam(UUID projectId, UUID userId) {
        Project project = getProjectById(projectId);

        boolean noUserInProject = project.getTeam().stream()
                .map(User::getId)
                .noneMatch(teamMemberId -> teamMemberId.equals(userId));

        if (noUserInProject) {
            throw new UserNotFoundException(String.format("No such user with id %s in project %s", userId, projectId));
        }

        project.getTeam().removeIf(user -> user.getId().equals(userId));

        return project;
    }

}
