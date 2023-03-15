package com.dev.exception;

/**
 * @author : JasonMao
 * @version : 1.0
 * @date : 2023/3/15 10:55
 * @description :
 */
public class VerificationException extends RuntimeException{

    public VerificationException() {
    }

    public VerificationException(String message) {
        super(message);
    }

    public VerificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public VerificationException(Throwable cause) {
        super(cause);
    }

    public VerificationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
