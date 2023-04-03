package com.innowise.userserver.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.userserver.dao.exception.DAOException;
import com.innowise.userserver.dao.impl.UserDaoMysql;
import com.innowise.userserver.model.User;
import com.innowise.userserver.service.PasswordHasher;
import com.innowise.userserver.service.UserService;
import com.innowise.userserver.service.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
@Slf4j
public class UserRestService implements UserService {

    private static UserRestService instance = null;
    private static UserDaoMysql userDao;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String LOGIN_PARAMETER = "login";
    private static final String LOGIN_ERROR_MESSAGE = "Login input is incorrect";

    private static final String GET_USER_ERROR_MESSAGE = "Unknown error, while retrieving user";

    private static final String LOGIN_INVALID_MESSAGE = "Invalid login. Login can't be empty";
    private static final String ERROR_PASSWORD_SAVE = "Password saving caused an error";

    private static final PasswordHasher PASSWORD_HASHER = PasswordHasher.getInstance();

    private UserRestService() {
        try {
            userDao = UserDaoMysql.getInstance();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public static UserRestService getInstance() {
        if (instance == null) {
            instance = new UserRestService();
        }
        return instance;
    }

    @Override
    public String getAllUsersJson() throws DAOException, ServiceException {
        List<User> userList = userDao.getAllUsers();
        try {
            String json = OBJECT_MAPPER.writeValueAsString(userList);
            return json;
        } catch (JsonProcessingException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean isUserAdmin(HttpServletRequest request) throws ServiceException, DAOException {
        String login = request.getParameter(LOGIN_PARAMETER);
        if(login == null || login.isBlank()){
            throw new ServiceException(LOGIN_ERROR_MESSAGE);
        }
        Optional<User> userOptional = userDao.getUserByLogin(login);
        if(!userOptional.isPresent()){
            throw new ServiceException(GET_USER_ERROR_MESSAGE);
        }
        User user = userOptional.get();
        return user.getRole().equals(User.Role.ADMIN);
    }

    @Override
    public void createNewUser(HttpServletRequest request) throws DAOException, ServiceException {
        userDao.createUser(ExtractUserFromRequest(request));
    }

    @Override
    public void updateUser(HttpServletRequest request) throws DAOException, ServiceException {
        userDao.updateUser(ExtractUserFromRequest(request));
    }

    private User ExtractUserFromRequest(HttpServletRequest request) throws ServiceException{
        User user;
        try {
            user = OBJECT_MAPPER.readValue(request.getInputStream(), User.class);
            log.debug("User extracted from request "+((user == null)?"null":user.toString()));
            Optional<String> passwordOptional = PASSWORD_HASHER.hashPassword(user.getPassword());
            if(passwordOptional.isPresent()){
                user.setPassword(passwordOptional.get());
            }else{
                throw new ServiceException(ERROR_PASSWORD_SAVE);
            }
        } catch (IOException e) {
            throw new ServiceException(e);
        }
        return user;
    }

    @Override
    public void deleteUser(HttpServletRequest request) throws ServiceException, DAOException {
        String login = request.getParameter(LOGIN_PARAMETER);
        if (login != null && !login.isBlank()) {//TODO add auth check after normal roles impl
            userDao.deleteUser(login);
        } else {
            throw new ServiceException(LOGIN_INVALID_MESSAGE);
        }
    }
}
