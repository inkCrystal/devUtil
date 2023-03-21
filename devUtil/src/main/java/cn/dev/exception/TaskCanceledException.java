package cn.dev.exception;

public class TaskCanceledException extends RuntimeException{
    public TaskCanceledException() {
    }

    public TaskCanceledException(String message) {
        super(message);
    }

    public TaskCanceledException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskCanceledException(Throwable cause) {
        super(cause);
    }

    public TaskCanceledException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
