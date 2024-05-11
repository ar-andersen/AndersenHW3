package com.rybak.andersenhw3.dao;

import com.rybak.andersenhw3.config.HibernateUtil;
import com.rybak.andersenhw3.entity.Comment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.UUID;

public class CommentDao {

    private static final String GET_ALL_COMMENTS_BY_TASK_ID = "SELECT c FROM Comment c WHERE c.task.id = :taskId";

    public void saveComment(Comment comment) {
        Session session = HibernateUtil.openSession();
        Transaction transaction = session.beginTransaction();

        session.persist(comment);

        transaction.commit();
        session.close();
    }

    public List<Comment> getAllCommentsByTaskId(UUID taskId) {
        Session session = HibernateUtil.openSession();

        List<Comment> comments = session.createQuery(GET_ALL_COMMENTS_BY_TASK_ID, Comment.class)
                .setParameter("taskId", taskId)
                .list();

        session.close();

        return comments;
    }

}
