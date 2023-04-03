package com.innowise.userserver.service.impl;

import com.innowise.userserver.dao.impl.UserDaoMysql;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthRestServiceTest {

    @Test
    void getInstance() {
        assertNotNull(AuthRestService.getInstance());
    }
}