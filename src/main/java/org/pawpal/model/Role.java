package org.pawpal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    Long id;

    @Column(name = "name")
    String name;

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role() {}
}
