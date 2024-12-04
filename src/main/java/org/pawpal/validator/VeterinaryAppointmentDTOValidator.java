package org.pawpal.validator;

import org.pawpal.dto.VeterinaryAppointmentDTO;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Configuration
public class VeterinaryAppointmentDTOValidator implements Validator {

  @Override
  public boolean supports(Class<?> objectClass) {
    return VeterinaryAppointmentDTO.class.equals(objectClass);
  }

  @Override
  public void validate(Object target, Errors errors) {
    VeterinaryAppointmentDTO appointmentDTO = (VeterinaryAppointmentDTO) target;
    if (appointmentDTO.getPetId() == null) {
      errors.rejectValue("petId", "petId.empty", "Pet ID cannot be empty");
    }
    if(appointmentDTO.getUserId() == null) {
      errors.rejectValue("userId", "userId.empty", "User ID cannot be empty");
    }
    if (appointmentDTO.getLocalDateTime() == null) {
      errors.rejectValue("appointmentDate", "appointmentDate.empty", "Appointment date cannot be empty");
    }
    if (LocalDateTime.now().isAfter(appointmentDTO.getLocalDateTime())) {
      errors.rejectValue("appointmentDate", "appointmentDate.invalid", "Appointment date cannot be in the past");
    }
    if(appointmentDTO.getDuration() <= 0) {
      errors.rejectValue("duration", "duration.invalid", "Duration must be greater than 0");
    }
    if(appointmentDTO.getCost() < 0) {
      errors.rejectValue("cost", "cost.invalid", "Cost must be greater than 0");
    }
  }

  public boolean isValid(VeterinaryAppointmentDTO appointmentDTO) {
    Errors errors = new BeanPropertyBindingResult(appointmentDTO, "appointmentDTO");
    validate(appointmentDTO, errors);
    return !errors.hasErrors();
  }
}
