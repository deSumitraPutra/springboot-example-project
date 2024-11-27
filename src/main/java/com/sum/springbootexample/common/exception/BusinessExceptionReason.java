package com.sum.springbootexample.common.exception;

import com.sum.springbootexample.common.exception.policy.BusinessExceptionPolicy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BusinessExceptionReason implements BusinessExceptionPolicy {

    BOOK_NOT_FOUND_BY_ID("Book not found by id: %s", "V147", HttpStatus.NOT_FOUND);

    private final String message;
    private final String code;
    private final HttpStatus httpStatus;

}
