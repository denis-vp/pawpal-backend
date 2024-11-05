package org.pawpal.dto;

public class PetDTO {
    private Long id;
    private String name;
    private String breed;
    private int age;
    private int weight;
    private String medicalHistory;
    private String email;


    public PetDTO(Long id, String name, String breed, int age, int weight, String medicalHistory, String email) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.email = email;
    }

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

    public String getEmail() {
        return email;
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

    public void setEmail(String email) {
        this.email = email;
    }
}
