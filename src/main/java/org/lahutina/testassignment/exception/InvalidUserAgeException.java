package org.lahutina.testassignment.exception;

/**
 * Exception thrown when the age of a user is invalid.
 */
public class InvalidUserAgeException extends RuntimeException {
    public InvalidUserAgeException(String message) {
        super(message);
    }
}
