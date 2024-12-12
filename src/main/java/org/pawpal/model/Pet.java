package org.pawpal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "pets")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String name;

    @Column
    String breed;

    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

    @Column
    AnimalType type;

    @Column
    double weight;

    @Column(name = "is_male")
    boolean isMale;

    @Lob
    @Column(name = "image_data")
    private byte[] imageData;

    @Column
    String imageType;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
}
