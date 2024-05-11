package com.rybak.andersenhw3.dao;

import com.rybak.andersenhw3.config.HibernateUtil;
import com.rybak.andersenhw3.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.UUID;

public class UserDao {

    private static final String GET_USER_BY_EMAIL = "SELECT u FROM User u WHERE email = :email";
    private static final String GET_ALL_USERS = "SELECT u FROM User u";

    public boolean existsByEmail(String email) {
        Session session = HibernateUtil.openSession();

        User user = session.createQuery(GET_USER_BY_EMAIL, User.class)
                .setParameter("email", email)
                .uniqueResult();

        session.close();

        return user != null;
    }

    public void insertUser(User user) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();

        session.persist(user);

        transaction.commit();
        session.close();
    }

    public User findUserByEmail(String email) {
        Session session = HibernateUtil.openSession();

        User user = session.createQuery(GET_USER_BY_EMAIL, User.class)
                .setParameter("email", email)
                .uniqueResult();

        session.close();

        return user;
    }

    public User findUserById(UUID id) {
        Session session = HibernateUtil.openSession();

        User user = session.get(User.class, id);

        session.close();

        return user;
    }

    public boolean deleteUserById(UUID id) {
        try (Session session = HibernateUtil.openSession()) {
            Transaction transaction = session.beginTransaction();

            User user = session.get(User.class, id);
            if (user == null) {
                return false;
            }
            session.remove(user);

            transaction.commit();

            return true;
        }
    }

    public List<User> getAllUsers() {
        Session session = HibernateUtil.openSession();

        List<User> users = session.createQuery(GET_ALL_USERS, User.class).list();

        session.close();

        return users;
    }

}
