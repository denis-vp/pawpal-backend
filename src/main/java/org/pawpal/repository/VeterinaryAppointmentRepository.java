package org.pawpal.repository;

import org.pawpal.model.VeterinaryAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeterinaryAppointmentRepository extends JpaRepository<VeterinaryAppointment,Long> {
}
