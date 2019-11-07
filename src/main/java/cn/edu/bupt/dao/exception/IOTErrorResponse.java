package cn.edu.bupt.dao.exception;

import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * Created by CZX on 2018/4/20.
 */
public class IOTErrorResponse {

    // HTTP Response Status Code
    private final HttpStatus status;

    // General Error message
    private final String message;

    // Error code
    private final IOTErrorCode errorCode;

    private final Date timestamp;

    protected IOTErrorResponse(final String message, final IOTErrorCode errorCode, HttpStatus status) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.timestamp = new Date();
    }

    public static IOTErrorResponse of(final String message, final IOTErrorCode errorCode, HttpStatus status) {
        return new IOTErrorResponse(message, errorCode, status);
    }

    public Integer getStatus() {
        return status.value();
    }

    public String getMessage() {
        return message;
    }

    public IOTErrorCode getErrorCode() {
        return errorCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
