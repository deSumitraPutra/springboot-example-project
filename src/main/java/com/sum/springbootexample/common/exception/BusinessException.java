package com.sum.springbootexample.common.exception;

import com.sum.springbootexample.common.exception.policy.BusinessExceptionPolicy;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BusinessException extends RuntimeException implements BusinessExceptionPolicy {

    protected final HttpStatus httpStatus;
    protected final String code;
    protected final String message;
    protected final String source;

    public BusinessException(final BusinessExceptionReason reason) {
        this.httpStatus = reason.getHttpStatus();
        this.code = reason.getCode();
        this.message = reason.getMessage();
        this.source = this.getClass().getSimpleName();
    }

    public BusinessException(final BusinessExceptionReason reason, final Object... parameters) {
        if (parameters != null) {
            this.message = String.format(reason.getMessage(), parameters);
        } else {
            this.message = reason.getMessage();
        }

        this.httpStatus = reason.getHttpStatus();
        this.source = this.getClass().getSimpleName();
        this.code = reason.getCode();
    }

    @Override
    public String getLocalizedMessage() {
        return this.message;
    }

    public String toString() {
        String template = "BusinessException(httpStatus=%s, code=%s, message=%s, source=%s)";
        return String.format(template, this.httpStatus, this.code, this.message, this.source);
    }

}
