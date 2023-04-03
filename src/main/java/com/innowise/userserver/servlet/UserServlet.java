package com.innowise.userserver.servlet;

import com.innowise.userserver.dao.exception.DAOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

import com.innowise.userserver.service.AuthService;
import com.innowise.userserver.service.UserService;
import com.innowise.userserver.service.exception.ServiceException;
import com.innowise.userserver.service.impl.AuthRestService;
import com.innowise.userserver.service.impl.UserRestService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebServlet("/Userbase")
public class UserServlet extends HttpServlet {
    private static final String JSON_CONTENT_TYPE = "application/json; charset=UTF-8";
    private static UserService userService;
    private static AuthService authService;

    private static final String LOGIN_PARAMETER = "login";

    private static final String USER_DELETED_MESSAGE = "User %s was successfully deleted by %s";

    private static final String USER_UPDATED_MESSAGE = "User %s was successfully updated by %s";


    @Override
    public void init() {
        authService = AuthRestService.getInstance();
        userService = UserRestService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (isLoggedIn(request)) {
                String json = userService.getAllUsersJson();
                response.setContentType(JSON_CONTENT_TYPE);
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.write(json);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) { //create
        try {
            if (isLoggedIn(request) && isAdmin(request)) {
                userService.createNewUser(request);
                response.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {//update
        try {
            if (isLoggedIn(request) && isAdmin(request)) {
                userService.updateUser(request);
                response.setStatus(HttpServletResponse.SC_CREATED);
                HttpSession session = request.getSession(false);
                String executorLogin = session.getAttribute(LOGIN_PARAMETER).toString();
                String deletedLogin = request.getParameter(LOGIN_PARAMETER);
                log.warn(String.format(USER_UPDATED_MESSAGE, deletedLogin, executorLogin));
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (isLoggedIn(request) && isAdmin(request)) {
                userService.deleteUser(request);
                response.setStatus(HttpServletResponse.SC_OK);
                HttpSession session = request.getSession(false);
                String executorLogin = session.getAttribute(LOGIN_PARAMETER).toString();
                String deletedLogin = request.getParameter(LOGIN_PARAMETER);
                log.warn(String.format(USER_DELETED_MESSAGE, deletedLogin, executorLogin));
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (DAOException e) {
            log.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        String login = session.getAttribute(LOGIN_PARAMETER).toString();
        if (login == null || login.isBlank()) {
            return false;
        }
        return true;
    }

    private boolean isAdmin(HttpServletRequest request) throws DAOException, ServiceException {
        return userService.isUserAdmin(request);
    }

}