package me.goudham.exception;

/**
 * Thrown when {@link me.goudham.MyClipboard} detects an operating system which is not {@code Mac} or {@code Windows/*Unix}
 */
public class UnsupportedSystemException extends Throwable {
    public UnsupportedSystemException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
