package com.rybak.andersenhw3.dao;

import com.rybak.andersenhw3.config.HibernateUtil;
import com.rybak.andersenhw3.entity.Task;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.UUID;

public class TaskDao {

    private static final String GET_TASK_BY_ID = "SELECT t FROM Task t LEFT JOIN FETCH t.comments c WHERE t.id = :id";
    private static final String GET_ALL_TASKS = "SELECT t FROM Task t LEFT JOIN FETCH t.comments c WHERE t.project.id = :id";

    public void saveTask(Task task) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();

        session.persist(task);

        transaction.commit();
        session.close();
    }

    public Task getTaskById(UUID taskId) {
        Session session = HibernateUtil.openSession();

        Task task = session.createQuery(GET_TASK_BY_ID, Task.class)
                .setParameter("id", taskId)
                .uniqueResult();

        session.close();

        return task;
    }

    public List<Task> getAllTasks(UUID projectId) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();

        List<Task> tasks = session.createQuery(GET_ALL_TASKS, Task.class)
                .setParameter("id", projectId)
                .list();

        transaction.commit();
        session.close();

        return tasks;
    }

    public boolean deleteTask(UUID taskId) {
        try(Session session = HibernateUtil.openSession()) {
            Transaction transaction = session.beginTransaction();

            Task task = session.get(Task.class, taskId);
            if (task == null) {
                return false;
            }
            session.remove(task);

            transaction.commit();
            return true;
        }
    }

    public void updateTask(Task task) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();

        session.merge(task);

        transaction.commit();
        session.close();
    }

}
