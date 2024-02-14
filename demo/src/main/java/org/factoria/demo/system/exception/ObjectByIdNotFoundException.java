package org.factoria.demo.system.exception;

public class ObjectByIdNotFoundException extends RuntimeException{
    public ObjectByIdNotFoundException(String objectName, Long id) {
        super("Could not find "+ objectName +  " with id: " + id);
    }
}
