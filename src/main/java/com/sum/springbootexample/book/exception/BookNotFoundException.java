package com.sum.springbootexample.book.exception;

import com.sum.springbootexample.common.exception.BusinessException;
import com.sum.springbootexample.common.exception.BusinessExceptionReason;

public class BookNotFoundException extends BusinessException {
    public BookNotFoundException(BusinessExceptionReason reason) {
        super(reason);
    }
}
