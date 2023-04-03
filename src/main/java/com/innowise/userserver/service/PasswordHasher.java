package com.innowise.userserver.service;

import com.innowise.userserver.dao.impl.UserDaoMysql;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;
@Slf4j
public class PasswordHasher{

    private PasswordHasher(){}

    private static PasswordHasher instance = null;

    private static final String ALGORITHM_NAME = "SHA-256";

    public static PasswordHasher getInstance(){
        if(instance == null) {
            instance = new PasswordHasher();
        }
        return instance;
    }

    public Optional<String> hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM_NAME);
            byte[] hashBytes = md.digest(password.getBytes());
            String hash = Base64.getEncoder().encodeToString(hashBytes);
            return Optional.of(hash);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(),e);
            return Optional.empty();
        }
    }
}
