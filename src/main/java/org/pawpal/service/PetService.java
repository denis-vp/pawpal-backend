package org.pawpal.service;

import org.pawpal.dto.PetDTO;
import org.pawpal.model.Pet;
import org.pawpal.model.User;
import org.pawpal.repository.PetRepository;
import org.pawpal.repository.UserRepository;
import org.pawpal.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    public List<PetDTO> getAllPets() {
        return petRepository.findAll().stream()
                .map(MapperUtil::toPetDTO)
                .toList();
    }

    public List<PetDTO> getPetsByUserId(Long userId) {
        return petRepository.findAll().stream()
                .filter(p -> Objects.equals(p.getOwner().getId(), userId))
                .map(MapperUtil::toPetDTO)
                .toList();
    }

    public PetDTO getPetById(Long id) {
        if (petRepository.existsById(id)) {
            return MapperUtil.toPetDTO(petRepository.getOne(id));
        } else throw new RuntimeException("Pet not found");
    }

    public PetDTO createPet(PetDTO petDTO) {
        User user = userRepository.findByEmail(petDTO.getEmail());
        Pet pet = MapperUtil.toPet(petDTO, user);
        return MapperUtil.toPetDTO(petRepository.save(pet));
    }

    public PetDTO updatePet(Long id, PetDTO petDTO) {
        if (petRepository.existsById(id)) {
            Pet existingPet = petRepository.getOne(id);
            existingPet.setName(petDTO.getName());
            existingPet.setAge(petDTO.getAge());
            existingPet.setBreed(petDTO.getBreed());
            existingPet.setWeight(petDTO.getWeight());
            existingPet.setMedicalHistory(petDTO.getMedicalHistory());
            Pet updatedPet = petRepository.save(existingPet);
            return MapperUtil.toPetDTO(updatedPet);
        } else throw new RuntimeException("Pet not found");
    }

    public void deletePet(Long id) {
        if (petRepository.existsById(id)) {
            petRepository.deleteById(id);
        } else throw new RuntimeException("Pet not found");
    }

}
