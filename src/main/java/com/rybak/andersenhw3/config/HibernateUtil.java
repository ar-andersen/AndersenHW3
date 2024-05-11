package com.rybak.andersenhw3.config;

import com.rybak.andersenhw3.entity.Comment;
import com.rybak.andersenhw3.entity.Project;
import com.rybak.andersenhw3.entity.Task;
import com.rybak.andersenhw3.entity.User;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.sql.DataSource;

public class HibernateUtil {

    private HibernateUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                    .applySetting("hibernate.connection.datasource", createDataSource())
                    .build();

            Metadata metadata = new MetadataSources(standardRegistry)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Project.class)
                    .addAnnotatedClass(Comment.class)
                    .addAnnotatedClass(Task.class)
                    .buildMetadata();

            return metadata.getSessionFactoryBuilder().build();
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static DataSource createDataSource() {
        String url = System.getProperty("DB.URL");
        String username = System.getProperty("DB.USERNAME");
        String password = System.getProperty("DB.PASSWORD");


        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);

        HikariDataSource dataSource = new HikariDataSource(config);

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .load();

        flyway.migrate();

        return dataSource;
    }

    public static Session openSession() {
        return sessionFactory.openSession();
    }

}
