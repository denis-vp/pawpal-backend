package org.pawpal.service;

import lombok.AllArgsConstructor;
import org.pawpal.dto.PetDTO;
import org.pawpal.exception.DeleteRecordException;
import org.pawpal.exception.ResourceNotFoundException;
import org.pawpal.exception.SaveRecordException;
import org.pawpal.model.Pet;
import org.pawpal.model.User;
import org.pawpal.repository.PetRepository;
import org.pawpal.util.MapperUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PetService {
    private PetRepository petRepository;

    public List<PetDTO> getPetsByUserEmail() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return petRepository.findAll().stream()
                .filter(p -> Objects.equals(p.getOwner().getEmail(), email))
                .map(MapperUtil::toPetDTO)
                .toList();
    }

    public PetDTO getPetById(Long id) {
        Optional<Pet> optionalPet = petRepository.findById(id);;
        if (optionalPet.isEmpty()) {
            throw new ResourceNotFoundException("Pet not found with ID: " + id);
        }
        return MapperUtil.toPetDTO(optionalPet.get());
    }

    public Pet findById(Long id){
        Optional<Pet> pet = petRepository.findById(id);
        if(pet.isPresent()){
            return pet.get();
        } else {
            throw new ResourceNotFoundException("Pet not found with ID: " + id);
        }
    }

    public PetDTO createPet(User user, PetDTO petDTO) throws IOException {
        Pet pet = MapperUtil.toPet(petDTO, user);
        try {
            return MapperUtil.toPetDTO(petRepository.save(pet));
        } catch (IllegalArgumentException exception) {
            throw new SaveRecordException("Error saving pet: " + exception.getMessage());
        } catch (RuntimeException exception) {
            throw new SaveRecordException("An unexpected error occurred when saving pet", exception);
        }
    }

    public PetDTO updatePet(Long id, PetDTO petDTO, User user) {
        Optional<Pet> optionalPet = petRepository.findById(id);
        if(optionalPet.isEmpty()){
            throw new ResourceNotFoundException("Pet not found with ID: " + id);
        }
        Pet pet = optionalPet.get();
        pet.setName(petDTO.getName());
        pet.setDateOfBirth(petDTO.getDateOfBirth());
        pet.setBreed(petDTO.getBreed());
        pet.setWeight(petDTO.getWeight());
        pet.setMale(petDTO.isMale());
        pet.setOwner(user);
        Pet updatedPet = petRepository.save(pet);
        return MapperUtil.toPetDTO(updatedPet);
    }

    public void deletePet(Long id)  {
        Optional<Pet> optionalPet = petRepository.findById(id);
        if(optionalPet.isEmpty()){
            throw new ResourceNotFoundException("Pet not found with ID: " + id);
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Pet pet = optionalPet.get();
        if(!pet.getOwner().getEmail().equals(email)) {
            throw new DeleteRecordException("You are not authorized to delete this pet");
        }
        petRepository.deleteById(id);
    }
}
