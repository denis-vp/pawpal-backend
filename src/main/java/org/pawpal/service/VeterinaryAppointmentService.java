package org.pawpal.service;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.pawpal.dto.VeterinaryAppointmentDTO;
import org.pawpal.exception.ResourceNotFoundException;
import org.pawpal.exception.SaveRecordException;
import org.pawpal.mail.EmailSender;
import org.pawpal.model.Pet;
import org.pawpal.model.User;
import org.pawpal.model.VeterinaryAppointment;
import org.pawpal.repository.VeterinaryAppointmentRepository;
import org.pawpal.util.MapperUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VeterinaryAppointmentService {
  private VeterinaryAppointmentRepository veterinaryAppointmentRepository;

  private UserService userService;
  private PetService petService;
  private EmailSender emailSender;

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

  public List<VeterinaryAppointment> getAppointmentsWithinADay(){
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime reminderThreshold = now.plusHours(24);

    List<VeterinaryAppointment> appointmentsToRemind = veterinaryAppointmentRepository.findAll().stream()
            .filter(appointment -> {
              LocalDateTime appointmentDateTime = appointment.getLocalDateTime();
              return appointmentDateTime.isAfter(reminderThreshold.minusMinutes(10)) &&
                      appointmentDateTime.isBefore(reminderThreshold.plusMinutes(10));
            })
            .toList();
    return appointmentsToRemind;
  }

  public VeterinaryAppointmentDTO createAppointment(VeterinaryAppointmentDTO appointmentDTO) throws ResourceNotFoundException, MessagingException {
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

    String recipient = user.getEmail();
    String subject = "Veterinary Appointment Confirmation";
    Map<String, Object> templateData = new HashMap<>();
    templateData.put("recipientName", user.getFirstName() + " " + user.getLastName());
    templateData.put("petName", pet.getName());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
    templateData.put("appointmentDateTime", appointment.getLocalDateTime().format(formatter));
    templateData.put("duration", appointment.getDuration());
    templateData.put("cost", appointment.getCost());
    templateData.put("status", appointment.getStatus());
    templateData.put("senderName", "PawPal Company");
    // temporary comment
//    emailSender.sendMailForNewAppointment(recipient, subject, templateData);
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

  @Scheduled(fixedRate = 600000)
  public void sendAppointmentReminders(){
    try{
      List<VeterinaryAppointment> appointmentsToRemind = getAppointmentsWithinADay();
      for(VeterinaryAppointment appointment : appointmentsToRemind){
        String recipient = appointment.getUser().getEmail();
        String subject = "Veterinary Appointment Reminder";
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("recipientName", appointment.getUser().getFirstName() + " " + appointment.getUser().getLastName());
        templateData.put("petName", appointment.getPet().getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
        templateData.put("appointmentDateTime", appointment.getLocalDateTime().format(formatter));
        templateData.put("senderName", "PawPal Company");
        emailSender.sendMailForAppointmentReminder(recipient, subject, templateData);
        System.out.println("sent");
      }
    }
    catch(MessagingException exception){
      System.out.println(exception.getMessage());
    }
  }

  public void deleteAppointment(Long id) {
    if(veterinaryAppointmentRepository.existsById(id))
      veterinaryAppointmentRepository.deleteById(id);
    else
      throw new ResourceNotFoundException("Veterinary Appointment not found with ID: " + id);
  }
}
