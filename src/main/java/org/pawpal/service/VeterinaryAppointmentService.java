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
  private UserRepository userRepository;

  @Autowired
  private PetRepository petRepository;

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

    Optional<User> user = userRepository.findById(appointment.getId());
    if(user.isPresent())
      appointment.setUser(user.get());
    else
      throw new ResourceNotFoundException("User not found with ID: " + appointment.getId());
    Optional<Pet> pet = petRepository.findById(appointment.getId());
    if(pet.isPresent())
      appointment.setPet(pet.get());
    else
      throw new ResourceNotFoundException("Pet not found with ID: " + appointment.getId());
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
