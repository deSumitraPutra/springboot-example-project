package com.sum.springbootexample.common.exception.policy;

import org.springframework.http.HttpStatus;

public interface ExceptionPolicy {

    HttpStatus getHttpStatus();

    String getMessage();

}
