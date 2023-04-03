package com.innowise.userserver.service.impl;

import com.innowise.userserver.dao.exception.DAOException;
import com.innowise.userserver.dao.impl.UserDaoMysql;
import com.innowise.userserver.model.User;
import com.innowise.userserver.service.AuthService;
import com.innowise.userserver.service.PasswordHasher;
import com.innowise.userserver.service.exception.ServiceException;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class AuthRestService implements AuthService {

    private static AuthRestService instance = null;
    private static UserDaoMysql userDao;

    private static final String LOGIN_PARAMETER = "login";

    private static final String PASSWORD_PARAMETER = "password";

    private static final String GET_USER_ERROR_MESSAGE = "Unknown error, while retrieving user";
    private static final String NO_SESSION_ERROR_MESSAGE = "User is already logged out";
    private static final String LOGIN_ERROR_MESSAGE = "Login input is incorrect";
    private static final String PASSWORD_ERROR_MESSAGE = "Password input is incorrect";
    private static final PasswordHasher PASSWORD_HASHER = PasswordHasher.getInstance();


    private AuthRestService(){
        try {
            userDao = UserDaoMysql.getInstance();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public static AuthRestService getInstance(){
        if(instance == null) {
            instance = new AuthRestService();
        }
        return instance;
    }
    @Override
    public boolean isCredentialsValid(HttpServletRequest request) throws ServiceException, DAOException {

        String login = request.getParameter(LOGIN_PARAMETER);
        String password = request.getParameter(PASSWORD_PARAMETER);
        if(login == null || login.isBlank()){
            throw new ServiceException(LOGIN_ERROR_MESSAGE);
        }
        if(password == null || password.isBlank()){
            throw new ServiceException(PASSWORD_ERROR_MESSAGE);
        }
        Optional<User> userOptional = userDao.getUserByLogin(login);
        if(!userOptional.isPresent()){
            throw new ServiceException(GET_USER_ERROR_MESSAGE);
        }
        User user = userOptional.get();
        String hashPassword = PASSWORD_HASHER.hashPassword(password).get();
        if (user.getLogin().equals(login) && user.getPassword().equals(hashPassword)) {
            return true;
        }
        return false;
    }

    @Override
    public void logout(HttpServletRequest request) throws ServiceException {
        HttpSession session = request.getSession(false);
        if(session == null){
            throw new ServiceException(NO_SESSION_ERROR_MESSAGE);
        }
        String login = session.getAttribute(LOGIN_PARAMETER).toString();
        if(login == null || login.isBlank()){
            throw new ServiceException(LOGIN_ERROR_MESSAGE);
        }
        session.invalidate();
    }
}
