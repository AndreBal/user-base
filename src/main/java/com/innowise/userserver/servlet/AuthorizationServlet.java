package com.innowise.userserver.servlet;

import com.innowise.userserver.dao.exception.DAOException;
import com.innowise.userserver.service.AuthService;
import com.innowise.userserver.service.exception.ServiceException;
import com.innowise.userserver.service.impl.AuthRestService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@WebServlet("/session")
public class AuthorizationServlet extends HttpServlet {

    private static AuthService authService;

    private static final String LOGIN_PARAMETER = "login";

    {
        authService = AuthRestService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (authService.isCredentialsValid(request)) {
                HttpSession session = request.getSession(true);
                session.setAttribute(LOGIN_PARAMETER, request.getParameter(LOGIN_PARAMETER));
                response.setStatus(HttpServletResponse.SC_OK);
            }else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (ServiceException | DAOException e) {
            log.error(e.getMessage(),e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        try {
            authService.logout(request);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (ServiceException e) {
            log.error(e.getMessage(),e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
