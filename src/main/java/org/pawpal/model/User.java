package org.pawpal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "firstName")
    String firstName;

    @Column(name = "lastName")
    String lastName;

    @Column(name = "email")
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "passwordAttempts")
    int passwordAttempts;

    @Column(name = "isNew")
    boolean isNew;

    public User() {}

    public User(Long id, String firstName, String lastName, String email, String password, int passwordAttempts, boolean isNew) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.passwordAttempts = passwordAttempts;
        this.isNew = isNew;
    }
}
