package com.BookMyShow.ApiGateway.Exceptions;

public class UserLoginException extends Exception {

    public UserLoginException() {
        super();
    }

    public UserLoginException(String message) {
        super(message);

    }

    public UserLoginException(String message, Throwable cause) {

        super(message, cause);
    }

}