package com.sum.springbootexample.book;

import com.sum.springbootexample.book.exception.BookNotFoundException;
import com.sum.springbootexample.common.exception.ApplicationException;
import com.sum.springbootexample.common.exception.ApplicationExceptionReason;
import com.sum.springbootexample.common.exception.BusinessExceptionReason;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository repository;

    public List<Book> getAll() {
        long nowLong = Instant.now().getEpochSecond();
        if (nowLong % 2 == 0) {
            throw new BookNotFoundException(BusinessExceptionReason.BOOK_NOT_FOUND_BY_ID);
        }
        throw new ApplicationException(ApplicationExceptionReason.BEAN_PROPERTY_NOT_EXISTS);
    }
}
