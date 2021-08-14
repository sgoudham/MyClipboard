package me.goudham.exception;

/**
 * Thrown when {@link java.awt.datatransfer.Clipboard} is unavailable to be accessed
 */
public class CannotAccessClipboardException extends Throwable {
    public CannotAccessClipboardException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
