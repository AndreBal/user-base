package com.innowise.userserver.service;

import com.innowise.userserver.dao.exception.DAOException;
import com.innowise.userserver.service.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    boolean isCredentialsValid(HttpServletRequest request) throws ServiceException, DAOException;

    void logout(HttpServletRequest request) throws ServiceException;
}
