package com.rybak.andersenhw3.dao;

import com.rybak.andersenhw3.config.HibernateUtil;
import com.rybak.andersenhw3.entity.Project;
import com.rybak.andersenhw3.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ProjectDao {

    private static final String GET_PROJECT_BY_ID = "SELECT p FROM Project p LEFT JOIN FETCH p.team LEFT JOIN FETCH p.tasks WHERE p.id = :id";
    private static final String GET_ALL_PROJECTS = "SELECT p FROM Project p LEFT JOIN FETCH p.team LEFT JOIN FETCH p.tasks";

    public void saveProject(Project project) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();

        session.persist(project);

        transaction.commit();
        session.close();
    }

    public void addUserToProject(UUID userId, UUID projectId) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();

        User user = session.get(User.class, userId);
        Project project = session.get(Project.class, projectId);
        project.addUser(user);

        transaction.commit();
        session.close();
    }

    public Project findProjectById(UUID projectId) {
        Session session = HibernateUtil.openSession();

        Project project = session.createQuery(GET_PROJECT_BY_ID, Project.class)
                .setParameter("id", projectId)
                .uniqueResult();

        session.close();

        return project;
    }

    public boolean deleteProjectById(UUID projectId) {
        try (Session session = HibernateUtil.openSession()) {
            Transaction transaction = session.beginTransaction();

            Project project = session.get(Project.class, projectId);

            if (project == null) {
                return false;
            }

            session.remove(project);

            transaction.commit();

            return true;
        }
    }

    public Set<User> getProjectTeamByProjectId(UUID projectId) {
        Session session = HibernateUtil.openSession();

        Project project = session.createQuery(GET_PROJECT_BY_ID, Project.class)
                .setParameter("id", projectId)
                .uniqueResult();

        session.close();

        if (project == null) {
            return new HashSet<>();
        }

        return project.getTeam();
    }

    public List<Project> getAllProjects() {
        Session session = HibernateUtil.openSession();

        List<Project> projects = session.createQuery(GET_ALL_PROJECTS, Project.class).list();

        session.close();

        return projects;
    }

    public void deleteUserFromProject(UUID projectId, UUID userId) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();

        Project project = session.get(Project.class, projectId);
        project.getTeam().removeIf(user -> user.getId().equals(userId));

        session.merge(project);

        transaction.commit();
        session.close();
    }

}
