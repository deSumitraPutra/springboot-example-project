package com.sum.springbootexample.common.exception;

import com.sum.springbootexample.common.exception.policy.ApplicationExceptionPolicy;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApplicationException extends RuntimeException implements ApplicationExceptionPolicy {

    private final HttpStatus httpStatus;
    private final String message;
    private final String source;

    public ApplicationException(final ApplicationExceptionReason reason) {
        this.httpStatus = reason.getHttpStatus();
        this.message = reason.getMessage();
        this.source = this.getClass().getSimpleName();
    }

    public ApplicationException(final ApplicationExceptionReason reason, final Object... parameters) {
        this.message = parameters != null
            ? String.format(reason.getMessage(), parameters)
            : reason.getMessage();

        this.httpStatus = reason.getHttpStatus();
        this.source = this.getClass().getSimpleName();
    }

    @Override
    public String getLocalizedMessage() {
        return this.message;
    }

    public String toString() {
        String template = "ApplicationException(httpStatus=%s, message=%s)";
        return String.format(template, this.getHttpStatus(), this.getMessage());
    }

}
