package com.example.studentresultmanagementsystem.exceptions;

//NotFoundException is a custom exception thrown when an entity that is being searched for does not exist in the system.
public class NotFoundException extends Exception{
    public NotFoundException(String message) {
        super(message);
    }
}
