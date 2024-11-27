package com.sum.springbootexample.book;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BookServiceTest {

    class Parent extends RuntimeException {
        public String printClassname() {
            return this.getClass().getSimpleName();
        }
    }

    class ChildException extends Parent {

    }

    @Test
    void getAll() {
        String classname = new ChildException().printClassname();
        Assertions.assertEquals("Child", classname);
    }
}