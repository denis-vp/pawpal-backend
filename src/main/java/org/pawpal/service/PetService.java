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

    public PetDTO getPetById(Long id) {
        return MapperUtil.toPetDTO(petRepository.getOne(id));
    }

    public PetDTO createPet(PetDTO petDTO) {
        User user=userRepository.findByEmail(petDTO.getEmail());
        Pet pet=MapperUtil.toPet(petDTO,user);
        return MapperUtil.toPetDTO(petRepository.save(pet));
    }

    public PetDTO updatePet(Long id,PetDTO petDTO) {
        if(petRepository.existsById(id)) {
            User user=userRepository.findByEmail(petDTO.getEmail());
            Pet pet=MapperUtil.toPet(petDTO,user);
            //pet.setId(id);//?
            return MapperUtil.toPetDTO(petRepository.save(pet));
        }
        else throw new RuntimeException("Pet not found");
    }
    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }

}
