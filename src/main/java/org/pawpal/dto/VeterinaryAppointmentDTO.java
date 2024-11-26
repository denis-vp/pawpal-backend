package org.pawpal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pawpal.model.AppointmentStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VeterinaryAppointmentDTO {
  private Long id;
  private Long userId;
  private Long petId;
  private AppointmentStatus status;
  private LocalDateTime localDateTime;
  private int duration;
  private Double cost;
}
