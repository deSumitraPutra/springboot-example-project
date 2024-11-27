package com.sum.springbootexample.book;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService service;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = this.service.getAll();

        return ResponseEntity.ok(books);
    }
}
