package com.miguel.AulaSpring.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message){
        super(message);
    }
}
