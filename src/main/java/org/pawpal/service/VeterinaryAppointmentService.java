package org.pawpal.service;

import org.pawpal.dto.VeterinaryAppointmentDTO;
import org.pawpal.exception.ResourceNotFoundException;
import org.pawpal.model.Pet;
import org.pawpal.model.User;
import org.pawpal.model.VeterinaryAppointment;
import org.pawpal.repository.PetRepository;
import org.pawpal.repository.UserRepository;
import org.pawpal.repository.VeterinaryAppointmentRepository;
import org.pawpal.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeterinaryAppointmentService {
  @Autowired
  private VeterinaryAppointmentRepository veterinaryAppointmentRepository;

  @Autowired
  private UserService userService;

  @Autowired
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

  public VeterinaryAppointmentDTO createAppointment(VeterinaryAppointmentDTO appointmentDTO) {
    VeterinaryAppointment appointment = new VeterinaryAppointment();

    try{
      User user = userService.findById(appointmentDTO.getUserId());
      appointment.setUser(user);
    } catch (ResourceNotFoundException exception) {
      throw exception;
    }

    try {
      Pet pet = petService.findById(appointmentDTO.getPetId());
      appointment.setPet(pet);
    } catch(ResourceNotFoundException exception) {
      throw exception;
    }

    appointment.setLocalDateTime(appointmentDTO.getLocalDateTime());
    appointment.setDuration(appointmentDTO.getDuration());
    appointment.setCost(appointmentDTO.getCost());

    appointment = veterinaryAppointmentRepository.save(appointment);

    appointmentDTO.setId(appointment.getId());
    appointmentDTO.setStatus(appointment.getStatus());
    return appointmentDTO;
  }

  public VeterinaryAppointmentDTO updateAppointment(Long id, VeterinaryAppointmentDTO appointmentDTO) {
    if(veterinaryAppointmentRepository.existsById(id)) {
      VeterinaryAppointment existingAppointment = veterinaryAppointmentRepository.getOne(id);
      existingAppointment.setLocalDateTime(appointmentDTO.getLocalDateTime());
      existingAppointment.setDuration(appointmentDTO.getDuration());
      existingAppointment.setCost(appointmentDTO.getCost());
      VeterinaryAppointment updatedAppointment = veterinaryAppointmentRepository.save(existingAppointment);
      return MapperUtil.toVeterinaryAppointmentDTO(updatedAppointment);
    } else
      throw new ResourceNotFoundException("Veterinary Appointment not found with ID: " + id);
  }

  public void deleteAppointment(Long id) {
    if(veterinaryAppointmentRepository.existsById(id))
      veterinaryAppointmentRepository.deleteById(id);
    else
      throw new ResourceNotFoundException("Veterinary Appointment not found with ID: " + id);
  }
}
