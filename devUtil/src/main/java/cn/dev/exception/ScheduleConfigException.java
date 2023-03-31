package cn.dev.exception;

public class ScheduleConfigException extends Exception{

    public ScheduleConfigException() {
    }

    public ScheduleConfigException(String message) {
        super(message);
    }

    public ScheduleConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScheduleConfigException(Throwable cause) {
        super(cause);
    }

    public ScheduleConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
