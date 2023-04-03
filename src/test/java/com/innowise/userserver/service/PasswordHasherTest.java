package com.innowise.userserver.service;

import com.innowise.userserver.dao.impl.UserDaoMysql;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHasherTest {

    @Test
    void getInstance() {
        assertNotNull(PasswordHasher.getInstance());
    }

    @Test
    void hashPassword() {
        assertEquals(PasswordHasher.getInstance().hashPassword("JohnDoe"),"Y9Zb/gMP9cuqwnu4ySFb97HGNbOo7X7prUXsz55LLi8=");
    }
}