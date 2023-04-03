package com.innowise.userserver.service.impl;

import com.innowise.userserver.dao.exception.DAOException;
import com.innowise.userserver.dao.impl.UserDaoMysql;
import com.innowise.userserver.service.exception.ServiceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserRestServiceTest {

    @Test
    void getInstance() {
        assertNotNull(UserRestService.getInstance());
    }

    @Test
    void getAllUsersJson() throws DAOException, ServiceException {assertNotNull(UserRestService.getInstance().getAllUsersJson());
    }

}