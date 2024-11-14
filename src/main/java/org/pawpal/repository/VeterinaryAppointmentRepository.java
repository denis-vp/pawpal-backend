package org.pawpal.repository;

import org.pawpal.model.VeterinaryAppointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeterinaryAppointmentRepository extends JpaRepository<VeterinaryAppointment,Long> {
}
