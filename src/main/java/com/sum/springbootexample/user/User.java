package com.sum.springbootexample.user;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String name;

    private String email;

    private String password;

    private String phone;

    private Instant createdAt;
}
