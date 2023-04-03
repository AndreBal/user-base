package com.innowise.userserver.dao.exception;

import java.io.Serial;

public class DAOException extends Exception {

    @Serial
    private static final long serialVersionUID = 2209493852968417846L;

    public DAOException(String message, Exception e) {
        super(message , e);
    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(Exception e) {
        super(e);
    }

    public DAOException() {
        super();
    }
}
