package com.mario.sample.reconciliation.exception;

/**
 *  Exception denoting that an issue interfering with a filter's normal operation has occurred.
 */
public class FilterException extends Exception {

    /**
     * {@inheritDoc}
     */
    public FilterException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public FilterException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

}