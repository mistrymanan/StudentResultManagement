package com.example.studentresultmanagementsystem.exceptions;

//AlreadyExistsException is a custom exception thrown when an entity that is being created already exists in the system.
public class AlreadyExistsException extends Exception {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
