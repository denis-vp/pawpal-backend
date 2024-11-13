package org.pawpal.service;

import org.pawpal.dto.PetDTO;
import org.pawpal.model.Pet;
import org.pawpal.model.User;
import org.pawpal.repository.PetRepository;
import org.pawpal.repository.UserRepository;
import org.pawpal.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    public List<PetDTO> getPetsByUserEmail() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return petRepository.findAll().stream()
                .filter(p -> Objects.equals(p.getOwner().getEmail(), email))
                .map(MapperUtil::toPetDTO)
                .toList();
    }

    public PetDTO getPetById(Long id) {
        if (petRepository.existsById(id)) {
            return MapperUtil.toPetDTO(petRepository.getOne(id));
        } else throw new RuntimeException("Pet not found");
    }

    public PetDTO createPet(PetDTO petDTO) throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).get();
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
            existingPet.setGender(petDTO.isGender());
            existingPet.setOwner(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get());
            Pet updatedPet = petRepository.save(existingPet);
            return MapperUtil.toPetDTO(updatedPet);
        } else throw new RuntimeException("Pet not found");
    }

    public void deletePet(Long id)  {
        if (petRepository.existsById(id)) {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            //User user = userRepository.findByEmail(email).get();
            Pet pet = petRepository.findById(id).get();
            if(pet.getOwner().getEmail().equals(email))
                petRepository.deleteById(id);
            else
                throw new RuntimeException("You are not authorized to delete this pet");
        } else throw new RuntimeException("Pet not found");
    }

}
