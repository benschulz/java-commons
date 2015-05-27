package de.benshu.commons.core.exception;

/**
 * An {@code UnexpectedCheckedException} thrown whenever a checked exception was not expected to be
 * thrown for whatever reason(s). The reason(s) should be clearly stated in the message and/or code
 * documentation when catching and wrapping the checked exception.
 * <p/>
 * For instance passing a {@code StringReader} to a decoder should not throw an {@code IOException},
 * thus it can be caught and wrapped into an {@code UnexpectedCheckedException}.
 */
public class UnexpectedCheckedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnexpectedCheckedException() {
        super();
    }

    public UnexpectedCheckedException(final String message) {
        super(message);
    }

    public UnexpectedCheckedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnexpectedCheckedException(final Throwable cause) {
        super(cause);
    }
}
