package de.benshu.commons.core.exception;

/**
 * Thrown when an unexpected branch is entered, e.g. from the default statement of a switch
 * statement that covered all enum constants.
 */
public class UnexpectedBranchException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnexpectedBranchException() {
        super();
    }

    public UnexpectedBranchException(final String message) {
        super(message);
    }

    public UnexpectedBranchException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnexpectedBranchException(final Throwable cause) {
        super(cause);
    }
}
