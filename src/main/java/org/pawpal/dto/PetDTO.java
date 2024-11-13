package org.pawpal.dto;

public class PetDTO {
    private Long id;
    private String name;
    private String breed;
    private int age;
    private int weight;
    private boolean gender;
    private String image;


    public PetDTO(Long id, String name, String breed, int age, int weight, String image, boolean gender) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.image = image;
        this.weight = weight;
        this.gender = gender;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}
