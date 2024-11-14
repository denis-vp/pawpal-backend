package org.pawpal.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "veterinary_appointments")
public class VeterinaryAppointment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  User user;

  @ManyToOne
  @JoinColumn(name = "pet_id", nullable = false)
  Pet pet;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  AppointmentStatus status = AppointmentStatus.SCHEDULED;

  @Column(name = "date", nullable = false)
  LocalDateTime localDateTime;

  @Column(name = "duration", nullable = false)
  int duration;

  @Column(name = "cost", nullable = false)
  Double cost;

  public VeterinaryAppointment() {}

  // Parameterized constructor
  public VeterinaryAppointment(User user, Pet pet, LocalDateTime localDateTime, int duration, Double cost) {
    this.user = user;
    this.pet = pet;
    this.localDateTime = localDateTime;
    this.duration = duration;
    this.cost = cost;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Pet getPet() {
    return pet;
  }

  public void setPet(Pet pet) {
    this.pet = pet;
  }

  public AppointmentStatus getStatus() {
    return status;
  }

  public void setStatus(AppointmentStatus status) {
    this.status = status;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public void setLocalDateTime(LocalDateTime localDateTime) {
    this.localDateTime = localDateTime;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public Double getCost() {
    return cost;
  }

  public void setCost(Double cost) {
    this.cost = cost;
  }
}
