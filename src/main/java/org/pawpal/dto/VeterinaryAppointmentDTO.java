package org.pawpal.dto;

import org.pawpal.model.AppointmentStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

public class VeterinaryAppointmentDTO {

  private Long id;
  private Long userId;
  private Long petId;
  private AppointmentStatus status;
  private LocalDateTime localDateTime;
  private int duration;
  private Double cost;

  public Long getId() {
    return id;
  }

  public VeterinaryAppointmentDTO(Long id, Long userId, Long petId, AppointmentStatus status, LocalDateTime localDateTime, int duration, Double cost) {
    this.id = id;
    this.userId = userId;
    this.petId = petId;
    this.status = status;
    this.localDateTime = localDateTime;
    this.duration = duration;
    this.cost = cost;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getPetId() {
    return petId;
  }

  public void setPetId(Long petId) {
    this.petId = petId;
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
