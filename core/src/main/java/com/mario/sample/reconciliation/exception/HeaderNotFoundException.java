package com.mario.sample.reconciliation.exception;

/**
 * Exception thrown when a record does not contain an expected header.
 */
public class HeaderNotFoundException extends Exception {

    /**
     * {@inheritDoc}
     */
    public HeaderNotFoundException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public HeaderNotFoundException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

}