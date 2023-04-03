package com.innowise.userserver.dao;

import com.innowise.userserver.dao.exception.DAOException;
import com.innowise.userserver.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDao {

    List<User> getAllUsers() throws DAOException;

    Optional<User> getUserByLogin(String login) throws DAOException;

    void deleteUser(String login) throws DAOException;

    Optional<User> createUser(User user) throws DAOException;

    Optional<User> updateUser(User user) throws DAOException;
}
