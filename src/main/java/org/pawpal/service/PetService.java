package org.pawpal.service;

import lombok.AllArgsConstructor;
import org.pawpal.dto.PetDTO;
import org.pawpal.exception.DeleteRecordException;
import org.pawpal.exception.ResourceNotFoundException;
import org.pawpal.exception.SaveRecordException;
import org.pawpal.model.AnimalType;
import org.pawpal.model.Pet;
import org.pawpal.model.User;
import org.pawpal.repository.PetRepository;
import org.pawpal.util.MapperUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
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

    public void addHardcodedPetsForUser(User user) {
        List<Pet> pets = Arrays.asList(
            new Pet(null, "Buddy", "Golden Retriever", LocalDate.of(2020, 5, 1), AnimalType.DOG, 30.0, true, null, user),
            new Pet(null, "Mittens", "Siamese", LocalDate.of(2019, 3, 15), AnimalType.CAT, 10.0, false, null, user),
            new Pet(null, "Tweety", "Canary", LocalDate.of(2021, 6, 20), AnimalType.BIRD, 0.5, true, null, user),
            new Pet(null, "Nibbles", "Hamster", LocalDate.of(2022, 8, 10), AnimalType.REPTILE, 0.2, false, null, user),
            new Pet(null, "Goldie", "Goldfish", LocalDate.of(2023, 1, 1), AnimalType.FISH, 0.1, true, null, user),
            new Pet(null, "Charlie", "Beagle", LocalDate.of(2018, 9, 30), AnimalType.DOG, 25.0, true, null, user),
            new Pet(null, "Luna", "Persian", LocalDate.of(2020, 2, 14), AnimalType.CAT, 12.0, false, null, user),
            new Pet(null, "Coco", "Cockatoo", LocalDate.of(2021, 4, 5), AnimalType.BIRD, 1.0, true, null, user),
            new Pet(null, "Spike", "Iguana", LocalDate.of(2019, 11, 11), AnimalType.REPTILE, 5.0, false, null, user),
            new Pet(null, "Bubbles", "Betta Fish", LocalDate.of(2023, 7, 15), AnimalType.FISH, 0.2, true, null, user),
            new Pet(null, "Max", "Rottweiler", LocalDate.of(2017, 12, 25), AnimalType.DOG, 40.0, true, null, user),
            new Pet(null, "Bella", "Maine Coon", LocalDate.of(2019, 2, 20), AnimalType.CAT, 15.0, false, null, user),
            new Pet(null, "Polly", "Parrot", LocalDate.of(2022, 3, 30), AnimalType.BIRD, 1.5, true, null, user),
            new Pet(null, "Ziggy", "Gecko", LocalDate.of(2020, 5, 25), AnimalType.REPTILE, 0.8, false, null, user),
            new Pet(null, "Finny", "Goldfish", LocalDate.of(2023, 8, 5), AnimalType.FISH, 0.1, true, null, user)
        );

        petRepository.saveAll(pets);
    }
}
