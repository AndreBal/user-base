package com.innowise.userserver.service;

import com.innowise.userserver.dao.exception.DAOException;
import com.innowise.userserver.service.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    String getAllUsersJson() throws DAOException, ServiceException;

    boolean isUserAdmin(HttpServletRequest request) throws ServiceException, DAOException;

    void createNewUser(HttpServletRequest request) throws DAOException, ServiceException;

    void updateUser(HttpServletRequest request) throws DAOException, ServiceException;

    void deleteUser(HttpServletRequest request) throws ServiceException, DAOException;
}
