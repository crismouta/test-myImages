package org.factoria.demo.system.exception;

public class BadCredentialsException extends RuntimeException{
    public BadCredentialsException(String username) {
        super(username + " password is incorrect" );
    }
}
