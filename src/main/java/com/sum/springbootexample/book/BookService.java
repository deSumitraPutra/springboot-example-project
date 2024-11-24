package com.sum.springbootexample.book;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository repository;

    public List<Book> getAll() {
        return this.repository.findAll();
    }
}
