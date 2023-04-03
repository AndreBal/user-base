package com.innowise.userserver.dao.impl;

import com.innowise.userserver.dao.UserDao;
import com.innowise.userserver.dao.exception.DAOException;
import com.innowise.userserver.model.User;
import com.innowise.userserver.service.PasswordHasher;
import lombok.extern.slf4j.Slf4j;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UserDaoMysql implements UserDao {

    private static UserDaoMysql instance = null;
    private static final String DATASOURCE_NAME = "java:comp/env/jdbc/userBase";

    private static final String ID_VAR = "id";
    private static final String LOGIN_VAR = "login";
    private static final String PASSWORD_VAR = "password";
    private static final String EMAIL_VAR = "email";
    private static final String PHONE_VAR = "phone";
    private static final String ROLE_ID_VAR  = "roles_idroles";

    private static final String NO_USER_MESSAGE = "No user with such login exists";

    private static final String SELECT_ALL_USERS = "SELECT * FROM users;";
    private static final String SELECT_USER_BY_LOGIN = "SELECT * FROM users WHERE login = ?;";
    private static final String CREATE_USER = "INSERT INTO users (`login`, `password`, `roles_idroles`, `email`, `phone`) VALUES (?, ?, ?, ?, ?);";
    private static final String DELETE_USER = "DELETE FROM users WHERE login = ?;";
    private static final String UPDATE_USER = "UPDATE users SET password = ?, roles_idroles = ?, email = ?, phone = ? WHERE (`id` = ?) and (`login` = ?);";

    private static InitialContext initContext;
    private static DataSource dataSource;

    private UserDaoMysql() throws NamingException {
        initContext = new InitialContext();
        dataSource = (DataSource) initContext.lookup(DATASOURCE_NAME);
    }

    public static UserDaoMysql getInstance() throws NamingException {
        if (instance == null) {
            instance = new UserDaoMysql();
        }
        return instance;
    }

    @Override
    public List<User> getAllUsers() throws DAOException {
        List<User> users = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(SELECT_ALL_USERS);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                users.add(userFromResultSet(rs));
            }

        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return users;
    }

    private User userFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt(ID_VAR));
        user.setLogin(rs.getString(LOGIN_VAR));
        user.setPassword(rs.getString(PASSWORD_VAR));
        user.setEmail(rs.getString(EMAIL_VAR));
        user.setPhone(rs.getString(PHONE_VAR));
        user.setRole(User.Role.fromId(rs.getInt(ROLE_ID_VAR)));
        return user;
    }

    @Override
    public Optional<User> getUserByLogin(String login) throws DAOException {

        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(SELECT_USER_BY_LOGIN)) {
            pst.setString(1, login);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(userFromResultSet(rs));
                } else {
                    throw new DAOException(NO_USER_MESSAGE);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void deleteUser(String login) throws DAOException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(DELETE_USER)) {
            pst.setString(1, login);
            pst.execute();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<User> createUser(User user) throws DAOException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(CREATE_USER)) {
            pst.setString(1, user.getLogin());
            pst.setString(2, user.getPassword());
            pst.setInt(3, user.getRole().getRoleId());
            pst.setString(4, user.getEmail());
            pst.setString(5, user.getPhone());
            if(pst.execute()){
                return Optional.of(user);
            }else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<User> updateUser(User user) throws DAOException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(UPDATE_USER)) {
            pst.setString(1, user.getPassword());
            pst.setInt(2, user.getRole().getRoleId());
            pst.setString(3, user.getEmail());
            pst.setString(4, user.getPhone());
            pst.setInt(5, user.getId());
            pst.setString(6, user.getLogin());
            log.info(pst.toString());
            if(pst.execute()){
                return Optional.of(user);
            }else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }
}
