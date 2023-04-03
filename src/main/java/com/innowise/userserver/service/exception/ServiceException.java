package com.innowise.userserver.service.exception;

import java.io.Serial;

public class ServiceException extends Exception {

    @Serial
    private static final long serialVersionUID = 2847268852948417846L;

    public ServiceException(String message, Exception e) {
        super(message , e);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Exception e) {
        super(e);
    }

    public ServiceException() {
        super();
    }
}
