package org.pawpal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "veterinary_appointments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
}
