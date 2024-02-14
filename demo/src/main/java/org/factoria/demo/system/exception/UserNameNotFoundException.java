package org.factoria.demo.system.exception;

public class UserNameNotFoundException extends RuntimeException{
    public UserNameNotFoundException(String username) {
        super("Username" + username + " is not found" );
    }
}
