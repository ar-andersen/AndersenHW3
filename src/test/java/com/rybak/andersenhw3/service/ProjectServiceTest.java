package com.rybak.andersenhw3.service;

import com.rybak.andersenhw3.entity.Project;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.ProjectNotFoundException;
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
class ProjectServiceTest {

    private final String NAME = "simple project";
    private final String DESCRIPTION = "simple project description";
    private final String EMAIL = "alex@gmail.com";
    private User user;
    private Project project;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setEmail(EMAIL);
        project = new Project(null, NAME, DESCRIPTION, null, null);
    }

    @AfterEach
    public void teardown() {
        GlobalStorage.users.clear();
        GlobalStorage.projects.clear();
    }

    @Test
    void createProject_WhenUserExist_ShouldCreateProject() {
        Mockito.when(userService.getUserByEmail(EMAIL)).thenReturn(user);

        Project actual = projectService.createProject(project, EMAIL);

        Assertions.assertEquals(project.getName(), actual.getName());
        Assertions.assertEquals(project.getDescription(), actual.getDescription());
        Assertions.assertNotNull(actual.getId());
    }

    @Test
    void createProject_WhenUserNotExist_ShouldThrowUserNotFoundException() {
        Mockito.when(userService.getUserByEmail(EMAIL)).thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, () -> projectService.createProject(project, EMAIL));
    }

    @Test
    void getProjectById_WhenProjectNotExist_ShouldThrowProjectNotFoundException() {
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name1", "description1", null, null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name2", "description2", null, null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name3", "description3", null, null));

        Assertions.assertThrows(ProjectNotFoundException.class, () -> projectService.getProjectById(UUID.randomUUID()));
    }

    @Test
    void getProjectById_WhenProjectExist_ShouldReturnProject() {
        UUID id = UUID.randomUUID();
        project.setId(id);
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name1", "description1", null, null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name2", "description2", null, null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name3", "description3", null, null));
        GlobalStorage.projects.add(project);

        Project actual = projectService.getProjectById(id);

        Assertions.assertEquals(project, actual);
    }

    @Test
    void deleteProjectById_WhenProjectExist_ShouldReturnTrue() {
        UUID id = UUID.randomUUID();
        GlobalStorage.projects.add(new Project(id, "name1", "description1", null, null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name2", "description2", null, null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name3", "description3", null, null));

        boolean actual = projectService.deleteProjectById(id);

        Assertions.assertTrue(actual);
    }

    @Test
    void deleteProjectById_WhenProjectNotExist_ShouldReturnFalse() {
        UUID id = UUID.randomUUID();
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name1", "description1", null, null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name2", "description2", null, null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name3", "description3", null, null));

        boolean actual = projectService.deleteProjectById(id);

        Assertions.assertFalse(actual);
    }

    @Test
    void getAllProjects_ShouldReturnCopiedList() {
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name1", "description1", null, null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name2", "description2", null, null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name3", "description3", null, null));

        List<Project> actual = projectService.getAllProjects();

        Assertions.assertEquals(GlobalStorage.projects.size(), actual.size());
    }

    @Test
    void addUserToTeam_WhenProjectNotExist_ShouldThrowProjectNotFoundException() {
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name1", "description1", null, null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name2", "description2", null, null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name3", "description3", null, null));

        Assertions.assertThrows(ProjectNotFoundException.class, () -> projectService.addUserToTeam(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    void addUserToTeam_WhenProjectExist_ShouldReturnProject() {
        UUID projectId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        project.setId(projectId);
        project.setTeam(new ArrayList<>());
        user.setId(userId);
        GlobalStorage.users.add(user);
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name1", "description1", new ArrayList<>(), null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name2", "description2", new ArrayList<>(), null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name3", "description3", new ArrayList<>(), null));
        GlobalStorage.projects.add(project);

        Project actual = projectService.addUserToTeam(projectId, userId);

        Assertions.assertEquals(project, actual);
        Assertions.assertEquals(1, actual.getTeam().size());
    }

    @Test
    void removeUserFromTeam_WhenProjectNotExist_ShouldThrowProjectNotFoundException() {
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name1", "description1", null, null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name2", "description2", null, null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name3", "description3", null, null));

        Assertions.assertThrows(ProjectNotFoundException.class, () -> projectService.removeUserFromTeam(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    void removeUserFromTeam_WhenProjectExist_ShouldRemoveUserFromProjectTeam() {
        UUID projectId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        project.setId(projectId);
        List<User> users = new ArrayList<>();
        users.add(user);
        project.setTeam(users);
        GlobalStorage.users.add(user);
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name1", "description1", new ArrayList<>(), null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name2", "description2", new ArrayList<>(), null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name3", "description3", new ArrayList<>(), null));
        GlobalStorage.projects.add(project);

        Project actual = projectService.removeUserFromTeam(projectId, userId);

        Assertions.assertEquals(project, actual);
        Assertions.assertEquals(0, actual.getTeam().size());
    }

    @Test
    void removeUserFromTeam_WhenUserIsNotExist_ShouldThrowUserNotFoundException() {
        UUID projectId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        project.setId(projectId);
        List<User> users = new ArrayList<>();
        users.add(user);
        project.setTeam(users);
        GlobalStorage.users.add(user);
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name1", "description1", new ArrayList<>(), null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name2", "description2", new ArrayList<>(), null));
        GlobalStorage.projects.add(new Project(UUID.randomUUID(), "name3", "description3", new ArrayList<>(), null));
        GlobalStorage.projects.add(project);

        Assertions.assertThrows(UserNotFoundException.class, () -> projectService.removeUserFromTeam(projectId, UUID.randomUUID()));
    }

}