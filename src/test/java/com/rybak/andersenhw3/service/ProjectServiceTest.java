package com.rybak.andersenhw3.service;

import com.rybak.andersenhw3.dao.ProjectDao;
import com.rybak.andersenhw3.entity.Project;
import com.rybak.andersenhw3.entity.User;
import com.rybak.andersenhw3.exception.ProjectNotFoundException;
import com.rybak.andersenhw3.exception.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProjectServiceTest {

    private final String NAME = "simple project";
    private final String DESCRIPTION = "simple project description";
    private final String EMAIL = "alex@gmail.com";
    private User user;
    private UUID userId = UUID.randomUUID();
    private Project project;

    @Mock
    private UserService userService;
    @Mock
    private ProjectDao projectDao;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setEmail(EMAIL);
        user.setId(userId);
        project = new Project(null, NAME, DESCRIPTION, null, null);
    }

    @Test
    void createProject_WhenUserExist_ShouldCreateProject() {
        Mockito.when(userService.getUserByEmail(EMAIL)).thenReturn(user);
        Mockito.doNothing().when(projectDao).saveProject(Mockito.any(Project.class));
        Mockito.doNothing().when(projectDao).addUserToProject(Mockito.any(), Mockito.any());

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
        Mockito.when(projectDao.findProjectById(Mockito.any())).thenReturn(null);

        Assertions.assertThrows(ProjectNotFoundException.class, () -> projectService.getProjectById(UUID.randomUUID()));
    }

    @Test
    void getProjectById_WhenProjectExist_ShouldReturnProject() {
        UUID id = UUID.randomUUID();
        project.setId(id);
        Set<User> team = new HashSet<>();
        project.setTeam(team);
        Mockito.when(projectDao.findProjectById(id)).thenReturn(project);
        Mockito.when(projectDao.getProjectTeamByProjectId(id)).thenReturn(team);

        Project actual = projectService.getProjectById(id);

        Assertions.assertEquals(project, actual);
    }

    @Test
    void deleteProjectById_WhenProjectExist_ShouldReturnTrue() {
        UUID id = UUID.randomUUID();
        Mockito.when(projectDao.deleteProjectById(id)).thenReturn(true);

        boolean actual = projectService.deleteProjectById(id);

        Assertions.assertTrue(actual);
    }

    @Test
    void deleteProjectById_WhenProjectNotExist_ShouldReturnFalse() {
        UUID id = UUID.randomUUID();
        Mockito.when(projectDao.deleteProjectById(id)).thenReturn(false);

        boolean actual = projectService.deleteProjectById(id);

        Assertions.assertFalse(actual);
    }

    @Test
    void getAllProjects_ShouldReturnCopiedList() {
        List<Project> projects = new ArrayList<>();
        projects.add(new Project(UUID.randomUUID(), "name1", "description1", null, null));
        projects.add(new Project(UUID.randomUUID(), "name2", "description2", null, null));
        projects.add(new Project(UUID.randomUUID(), "name3", "description3", null, null));
        Mockito.when(projectDao.getAllProjects()).thenReturn(projects);

        List<Project> actual = projectService.getAllProjects();

        Assertions.assertEquals(projects.size(), actual.size());
        Assertions.assertEquals(projects, actual);
    }

    @Test
    void addUserToTeam_WhenProjectNotExist_ShouldThrowProjectNotFoundException() {
        Mockito.when(projectDao.findProjectById(Mockito.any())).thenThrow(ProjectNotFoundException.class);

        Assertions.assertThrows(ProjectNotFoundException.class, () -> projectService.addUserToTeam(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    void addUserToTeam_WhenProjectExist_ShouldReturnProject() {
        UUID projectId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        project.setId(projectId);
        project.setTeam(new HashSet<>());
        user.setId(userId);
        List<Project> projects = new ArrayList<>();
        Set<User> team = new HashSet<>();
        team.add(user);
        projects.add(new Project(UUID.randomUUID(), "name1", "description1", new HashSet<>(), null));
        projects.add(new Project(UUID.randomUUID(), "name2", "description2", new HashSet<>(), null));
        projects.add(new Project(UUID.randomUUID(), "name3", "description3", new HashSet<>(), null));
        projects.add(project);
        Mockito.when(projectDao.findProjectById(projectId)).thenReturn(project);
        Mockito.when(projectDao.getProjectTeamByProjectId(projectId)).thenReturn(team);
        Mockito.when(userService.getUserByEmail(EMAIL)).thenReturn(user);
        Mockito.doNothing().when(projectDao).addUserToProject(Mockito.any(), Mockito.any());

        Project actual = projectService.addUserToTeam(projectId, userId);

        Assertions.assertEquals(project, actual);
    }

    @Test
    void removeUserFromTeam_WhenProjectNotExist_ShouldThrowProjectNotFoundException() {
        List<Project> projects = new ArrayList<>();
        projects.add(new Project(UUID.randomUUID(), "name1", "description1", null, null));
        projects.add(new Project(UUID.randomUUID(), "name2", "description2", null, null));
        projects.add(new Project(UUID.randomUUID(), "name3", "description3", null, null));

        Mockito.when(projectDao.findProjectById(Mockito.any())).thenReturn(null);

        Assertions.assertThrows(ProjectNotFoundException.class, () -> projectService.removeUserFromTeam(UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    void removeUserFromTeam_WhenProjectExist_ShouldRemoveUserFromProjectTeam() {
        UUID projectId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        project.setId(projectId);
        Set<User> users = new HashSet<>();
        users.add(user);
        project.setTeam(users);
        Set<User> team = new HashSet<>();
        team.add(user);
        List<Project> projects = new ArrayList<>();
        projects.add(new Project(UUID.randomUUID(), "name1", "description1", new HashSet<>(), null));
        projects.add(new Project(UUID.randomUUID(), "name2", "description2", new HashSet<>(), null));
        projects.add(new Project(UUID.randomUUID(), "name3", "description3", new HashSet<>(), null));
        projects.add(project);
        Mockito.when(projectDao.findProjectById(projectId)).thenReturn(project);
        Mockito.when(projectDao.getProjectTeamByProjectId(projectId)).thenReturn(team);
        Mockito.doNothing().when(projectDao).deleteUserFromProject(projectId, userId);

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
        Set<User> users = new HashSet<>();
        users.add(user);
        project.setTeam(users);
        Set<User> team = new HashSet<>();
        team.add(user);
        List<Project> projects = new ArrayList<>();
        projects.add(new Project(UUID.randomUUID(), "name1", "description1", new HashSet<>(), null));
        projects.add(new Project(UUID.randomUUID(), "name2", "description2", new HashSet<>(), null));
        projects.add(new Project(UUID.randomUUID(), "name3", "description3", new HashSet<>(), null));
        projects.add(project);
        Mockito.when(projectDao.findProjectById(projectId)).thenReturn(project);
        Mockito.when(projectDao.getProjectTeamByProjectId(projectId)).thenReturn(team);
        Mockito.doNothing().when(projectDao).deleteUserFromProject(projectId, userId);

        Assertions.assertThrows(UserNotFoundException.class, () -> projectService.removeUserFromTeam(projectId, UUID.randomUUID()));
    }

}