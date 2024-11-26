package org.pawpal.controller;

import lombok.AllArgsConstructor;
import org.pawpal.dto.VeterinaryAppointmentDTO;
import org.pawpal.exception.ResourceNotFoundException;
import org.pawpal.exception.SaveRecordException;
import org.pawpal.model.VeterinaryAppointment;
import org.pawpal.service.VeterinaryAppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/veterinary-appointments")
@AllArgsConstructor
public class VeterinaryAppointmentController {

  private final VeterinaryAppointmentService appointmentService;

  @GetMapping("/all")
  public ResponseEntity<List<VeterinaryAppointmentDTO>> getAllAppointments() {
    List<VeterinaryAppointmentDTO> appointments = appointmentService.getAllAppointments();
    if(appointments.isEmpty()){
      return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(appointments, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<VeterinaryAppointment> getAppointmentById(@PathVariable Long id) {
    try {
      VeterinaryAppointment appointment = appointmentService.getAppointmentById(id);
      return new ResponseEntity<>(appointment, HttpStatus.OK);
    } catch(ResourceNotFoundException exception) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/add")
  public ResponseEntity<VeterinaryAppointmentDTO> createAppointment(@RequestBody VeterinaryAppointmentDTO appointmentDTO) {
    try {
      VeterinaryAppointmentDTO createdAppointment = appointmentService.createAppointment(appointmentDTO);
      return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
    } catch(ResourceNotFoundException exception) {
      return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    } catch(SaveRecordException exception) {
      return new ResponseEntity<>(null, HttpStatus.CONFLICT);
    }catch (Exception exception) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<VeterinaryAppointmentDTO> updateAppointment(@PathVariable(name = "id") Long id, @RequestBody VeterinaryAppointmentDTO appointmentDTO){
    try {
      VeterinaryAppointmentDTO updatedAppointment = appointmentService.updateAppointment(id, appointmentDTO);
      return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
    } catch(ResourceNotFoundException exception) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    } catch(SaveRecordException exception) {
      return new ResponseEntity<>(null, HttpStatus.CONFLICT);
    } catch (Exception exception) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
    try {
      appointmentService.deleteAppointment(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch(ResourceNotFoundException exception) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
