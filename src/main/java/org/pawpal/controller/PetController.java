package org.pawpal.controller;

import org.pawpal.dto.PetDTO;
import org.pawpal.model.Pet;
import org.pawpal.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping("/all")
    public List<PetDTO> getPets() {
        return petService.getAllPets();
    }

    @GetMapping("/{id}")
    public PetDTO getPet(@PathVariable Long id) {
        return petService.getPetById(id);
    }

    @PostMapping("/add")
    public PetDTO createPet(@RequestBody PetDTO petDTO) {
        return petService.createPet(petDTO);
    }
    @PutMapping("/{id}")
    public PetDTO updatePet(@PathVariable Long id, @RequestBody PetDTO petDTO) {
        return petService.updatePet(id, petDTO);
    }

    @DeleteMapping("/{id}")
    public void deletePet(@PathVariable Long id) {
        petService.deletePet(id);
    }

}
