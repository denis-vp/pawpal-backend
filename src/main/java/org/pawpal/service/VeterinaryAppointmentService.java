package org.pawpal.service;

import lombok.AllArgsConstructor;
import org.pawpal.dto.VeterinaryAppointmentDTO;
import org.pawpal.exception.ResourceNotFoundException;
import org.pawpal.exception.SaveRecordException;
import org.pawpal.model.Pet;
import org.pawpal.model.User;
import org.pawpal.model.VeterinaryAppointment;
import org.pawpal.repository.VeterinaryAppointmentRepository;
import org.pawpal.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VeterinaryAppointmentService {
  private VeterinaryAppointmentRepository veterinaryAppointmentRepository;

  private UserService userService;
  private PetService petService;

  public List<VeterinaryAppointmentDTO> getAllAppointments() {
    return veterinaryAppointmentRepository.findAll().stream()
        .map(MapperUtil::toVeterinaryAppointmentDTO)
        .toList();
  }

  public VeterinaryAppointment getAppointmentById(Long id) {
    Optional<VeterinaryAppointment> appointment = veterinaryAppointmentRepository.findById(id);
    if(appointment.isPresent())
      return appointment.get();
    else
      throw new ResourceNotFoundException("Veterinary Appointment not found with ID: " + id);
  }

  public VeterinaryAppointmentDTO createAppointment(VeterinaryAppointmentDTO appointmentDTO) throws ResourceNotFoundException {
    VeterinaryAppointment appointment = new VeterinaryAppointment();

    User user = userService.findById(appointmentDTO.getUserId());
    appointment.setUser(user);

    Pet pet = petService.findById(appointmentDTO.getPetId());
    appointment.setPet(pet);

    appointment.setLocalDateTime(appointmentDTO.getLocalDateTime());
    appointment.setDuration(appointmentDTO.getDuration());
    appointment.setCost(appointmentDTO.getCost());

    try{
      appointment = veterinaryAppointmentRepository.save(appointment);
    } catch (IllegalArgumentException exception) {
      throw new SaveRecordException("Error creating appointment:" + exception.getMessage());
    } catch (RuntimeException exception) {
      throw new SaveRecordException("An unexpected error occurred while creating the appointment", exception);
    }

    appointmentDTO.setId(appointment.getId());
    appointmentDTO.setStatus(appointment.getStatus());
    return appointmentDTO;
  }

  public VeterinaryAppointmentDTO updateAppointment(Long id, VeterinaryAppointmentDTO appointmentDTO) {
    Optional<VeterinaryAppointment> optionalVeterinaryAppointment = veterinaryAppointmentRepository.findById(id);
    if(optionalVeterinaryAppointment.isEmpty())
      throw new ResourceNotFoundException("Veterinary Appointment not found with ID: " + id);
    else {
      VeterinaryAppointment appointment = optionalVeterinaryAppointment.get();
      appointment.setLocalDateTime(appointmentDTO.getLocalDateTime());
      appointment.setDuration(appointmentDTO.getDuration());
      appointment.setCost(appointmentDTO.getCost());
      try {
        VeterinaryAppointment updatedAppointment = veterinaryAppointmentRepository.save(appointment);
        return MapperUtil.toVeterinaryAppointmentDTO(updatedAppointment);
      } catch( IllegalArgumentException exception){
        throw new SaveRecordException("Error updating appointment:" + exception.getMessage());
      } catch (RuntimeException exception) {
        throw new SaveRecordException("An unexpected error occurred while updating the appointment", exception);
      }
    }
  }

  public void deleteAppointment(Long id) {
    if(veterinaryAppointmentRepository.existsById(id))
      veterinaryAppointmentRepository.deleteById(id);
    else
      throw new ResourceNotFoundException("Veterinary Appointment not found with ID: " + id);
  }
}
