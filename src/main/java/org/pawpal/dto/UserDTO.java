package org.pawpal.dto;

import java.util.List;

public class UserDTO {
    private Long id;
    private String firstName ;
    private String lastName;
    private String email;
    private String password;
    private int passwordAttempts;
    private boolean isNew;
    private List<PetDTO> pets;


    public UserDTO(Long id, String firstName, String lastName, String email, String password, int passwordAttempts, boolean isNew, List<PetDTO> pets) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.passwordAttempts = passwordAttempts;
        this.isNew = isNew;
        this.pets = pets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPasswordAttempts() {
        return passwordAttempts;
    }

    public void setPasswordAttempts(int passwordAttempts) {
        this.passwordAttempts = passwordAttempts;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public List<PetDTO> getPets() {
        return pets;
    }

    public void setPets(List<PetDTO> pets) {
        this.pets = pets;
    }
}
