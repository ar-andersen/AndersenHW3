package com.rybak.andersenhw3.service;

import com.rybak.andersenhw3.dao.ProjectDao;
import com.rybak.andersenhw3.entity.Project;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.ProjectNotFoundException;
import com.rybak.andersenhw3.exception.UserNotFoundException;

import java.util.List;
import java.util.UUID;

public class ProjectService {

    private final UserService userService;
    private final ProjectDao projectDao;

    public ProjectService(UserService userService, ProjectDao projectDao) {
        this.userService = userService;
        this.projectDao = projectDao;
    }

    public Project createProject(Project project, String email) {
        User user = userService.getUserByEmail(email);

        project.setId(UUID.randomUUID());
        projectDao.saveProject(project);
        projectDao.addUserToProject(user.getId(), project.getId());

        return project;
    }

    public Project getProjectById(UUID id) {
        Project project = projectDao.findProjectById(id);

        if (project == null) {
            throw new ProjectNotFoundException(String.format("Project with id %s not found", id));
        }

        List<User> team = projectDao.getProjectTeamByProjectId(id);
        project.setTeam(team);

        return project;
    }

    public boolean deleteProjectById(UUID id) {
        return projectDao.deleteProjectById(id);
    }

    public List<Project> getAllProjects() {
        return projectDao.getAllProjects();
    }

    public Project addUserToTeam(UUID projectId, UUID userId) {
        Project project = getProjectById(projectId);

        boolean noUserInProject = project.getTeam().stream()
                .map(User::getId)
                .noneMatch(teamMemberId -> teamMemberId.equals(userId));

        if (noUserInProject) {
            User user = userService.getUserById(userId);
            projectDao.addUserToProject(userId, projectId);
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

        projectDao.deleteUserFromProject(projectId, userId);
        project.getTeam().removeIf(user -> user.getId().equals(userId));

        return project;
    }

}
