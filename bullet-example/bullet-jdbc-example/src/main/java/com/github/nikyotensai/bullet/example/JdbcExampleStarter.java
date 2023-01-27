package com.github.nikyotensai.bullet.example;

import com.github.nikyotensai.bullet.example.dao.UserDAO;
import com.github.nikyotensai.bullet.jdbc.annotation.EnableConnectionManagement;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@MapperScan(basePackages = "com.github.nikyotensai.example.mapper")
@SpringBootApplication
@EnableConnectionManagement
public class JdbcExampleStarter {

    public static void main(final String[] args) {
        try (ConfigurableApplicationContext applicationContext = SpringApplication.run(JdbcExampleStarter.class, args)) {
            process(applicationContext);
        }
    }

    private static void process(final ConfigurableApplicationContext applicationContext) {
        UserDAO userDAO = getUserDAO(applicationContext);
        userDAO.testReadOnly();
        userDAO.testWrite();
    }

    private static UserDAO getUserDAO(final ConfigurableApplicationContext applicationContext) {
        return applicationContext.getBean(UserDAO.class);
    }
}
