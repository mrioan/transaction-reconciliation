package com.mario.sample.reconciliation.exception;

/**
 * Exception thrown when a file sent to the processing workflow is not supported for some reason.
 */
public class UnsupportedDataException extends RuntimeException {

    /**
     * {@inheritDoc}
     */
    public UnsupportedDataException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public UnsupportedDataException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

}