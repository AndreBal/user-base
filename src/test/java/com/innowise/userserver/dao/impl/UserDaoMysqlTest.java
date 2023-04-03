package com.innowise.userserver.dao.impl;

import com.innowise.userserver.dao.exception.DAOException;
import com.innowise.userserver.model.User;

import javax.naming.NamingException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoMysqlTest {

    @org.junit.jupiter.api.Test
    void getInstance() {
        try {
            assertNotNull(UserDaoMysql.getInstance());
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void getAllUsers() {
        try {
            UserDaoMysql userDao = UserDaoMysql.getInstance();
            List<User> users = userDao.getAllUsers();
            String usersString = users.stream().map(e -> e.toString()).reduce("", String::concat);
            System.out.println(usersString);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    void getUserByLogin() {
    }

    @org.junit.jupiter.api.Test
    void deleteUser() {
    }
}