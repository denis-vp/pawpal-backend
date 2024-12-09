package org.pawpal.validator;

import org.pawpal.dto.PetDTO;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Configuration
public class PetDTOValidator implements Validator {
  @Override
  public boolean supports(Class<?> objectClass) {
    return PetDTO.class.equals(objectClass);
  }

  @Override
  public void validate(Object target, Errors errors) {
    PetDTO petDTO = (PetDTO) target;
    if (petDTO.getName() == null || petDTO.getName().isEmpty()) {
      errors.rejectValue("name", "name.empty", "Name cannot be empty");
    }
    if (petDTO.getBreed() == null || petDTO.getBreed().isEmpty()) {
      errors.rejectValue("breed", "breed.empty", "Breed cannot be empty");
    }
    if (petDTO.getDateOfBirth() == null) {
      errors.rejectValue("dateOfBirth", "dateOfBirth.empty", "Date of birth cannot be empty");
    }
    if(LocalDate.now().isBefore(petDTO.getDateOfBirth())) {
      errors.rejectValue("dateOfBirth", "dateOfBirth.invalid", "Date of birth cannot be in the future");
    }
    if (petDTO.getType() == null) {
      errors.rejectValue("type", "type.empty", "Type cannot be empty");
    }
    if (petDTO.getWeight() <= 0) {
      errors.rejectValue("weight", "weight.invalid", "Weight must be greater than 0");
    }
  }

  public boolean isValid(PetDTO petDTO) {
    Errors errors = new BeanPropertyBindingResult(petDTO, "petDTO");
    validate(petDTO, errors);
    return !errors.hasErrors();
  }
}
