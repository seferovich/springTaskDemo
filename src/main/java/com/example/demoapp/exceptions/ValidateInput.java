package com.example.demoapp.exceptions;

import org.springframework.stereotype.Component;

@Component
public class ValidateInput {

    public void validate(String username, String password, String email) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty() || email == null || email.isEmpty()) {
            throw new InvalidInputException("Please fill in all of the fields!");
        }
    }
    public void validateLogin(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty() ) {
            throw new InvalidInputException("Please fill in all of the fields!");
        }
    }

    public static class InvalidInputException extends RuntimeException {
        public InvalidInputException(String message){
            super(message);
        }
    }
}
