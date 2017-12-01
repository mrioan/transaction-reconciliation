package com.mario.sample.reconciliation.exception;

/**
 * General exception indicating that there was some error during the reconciliation process.
 */
public class ReconciliationException extends RuntimeException {

    /**
     * {@inheritDoc}
     */
    public ReconciliationException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public ReconciliationException(String message, Throwable cause) {
        super(message, cause);
    }
}
