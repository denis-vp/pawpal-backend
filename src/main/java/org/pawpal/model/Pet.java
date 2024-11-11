package org.pawpal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String name;

    @Column
    String breed;

    @Column
    int age;

    @Column
    int weight;

    @Column
    String medicalHistory;

    @Lob
    @Column(name = "image_data")
    private byte[] imageData;

    public Pet() {
    }

    public Pet(String name, String breed, int age, int weight, String medicalHistory, byte[] imageData) {
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.medicalHistory = medicalHistory;
        this.imageData = imageData;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
